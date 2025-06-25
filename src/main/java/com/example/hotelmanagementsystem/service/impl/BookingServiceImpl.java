package com.example.hotelmanagementsystem.service.impl;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hotelmanagementsystem.exception.*;
import com.example.hotelmanagementsystem.dto.request.BookingRequestDTO;
import com.example.hotelmanagementsystem.dto.request.PaymentRequestDTO;
import com.example.hotelmanagementsystem.dto.response.BookingResponseDTO;
import com.example.hotelmanagementsystem.entity.*;
import com.example.hotelmanagementsystem.enums.BookingStatus;
import com.example.hotelmanagementsystem.mapper.BookingMapper;
import com.example.hotelmanagementsystem.repository.*;
import com.example.hotelmanagementsystem.service.*;

@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);
    
    SecureRandom random = new SecureRandom();

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final BookingMapper bookingMapper;
    private final PaymentService paymentService;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            RoomRepository roomRepository,
            GuestRepository guestRepository,
            BookingMapper bookingMapper,
            PaymentService paymentService) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.bookingMapper = bookingMapper;
        this.paymentService = paymentService;
    }

    @Transactional
    @Override  // create booking
    public BookingResponseDTO createBooking(BookingRequestDTO bookingDTO) {
        logger.info("Trying to create booking for email: {}", bookingDTO.getEmail());

        List<Room> allRooms = roomRepository.findByRoomTypeAndIsAvailableTrue(bookingDTO.getRoomType());
        Guest guest = validateGuestExists(bookingDTO.getEmail());

        Timestamp checkIn = bookingDTO.getCheckInDate();
        Timestamp checkOut = bookingDTO.getCheckOutDate();
        validateBookingDates(checkIn, checkOut);

        if (allRooms == null || allRooms.isEmpty()) {
            throw new RoomAlreadyBookedException("No available rooms for type: " + bookingDTO.getRoomType());
        }

        Room selectedRoom = findAvailableRoom(allRooms, checkIn, checkOut);

        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking.setRoom(selectedRoom);
        booking.setGuest(guest);

        roomRepository.save(selectedRoom);
        Booking savedBooking = bookingRepository.save(booking);

        PaymentRequestDTO payment = bookingDTO.getPayment();
        if (payment == null) {
            throw new PaymentInformationIsNullException("Payment information is required for booking.");
        }

        BigDecimal totalAmount = calculateTotalAmount(selectedRoom, checkIn, checkOut);
        payment.setRoomNumber(savedBooking.getRoom().getRoomNumber());
        payment.setEmail(savedBooking.getGuest().getEmail());
        payment.setAmountPaid(totalAmount);
        payment.setTransactionId(generateTransactionId());

        booking.setTotalAmount(totalAmount);
        paymentService.createPayment(payment);

        logger.info("Booking successfully created for room {} and guest {}", selectedRoom.getRoomNumber(), guest.getEmail());

        return bookingMapper.toResponseDTO(savedBooking);
    }

    @Override  //get all bookings
    public List<BookingResponseDTO> getAllBookings() {
        logger.info("Fetching all bookings...");
        List<Booking> bookings = bookingRepository.findAll();
        if (bookings.isEmpty()) {
            throw new BookingNotFoundException("No bookings found.");
        }

        List<BookingResponseDTO> dtoList = new ArrayList<>();
        for (Booking booking : bookings) {
            dtoList.add(bookingMapper.toResponseDTO(booking));
        }
        return dtoList;
    }

    @Override // update bookings
    public BookingResponseDTO updateBookingByRoomNumberAndEmail(String roomNumber, String email, BookingRequestDTO bookingRequestDTO) {
        logger.info("Updating booking for room: {}, email: {}", roomNumber, email);
        Booking booking = bookingRepository.findByRoom_RoomNumberAndGuest_Email(roomNumber, email);

        if (booking == null) {
            throw new BookingNotFoundException("No booking found for room number: " + roomNumber + " and email: " + email);
        }

        Timestamp newCheckIn = bookingRequestDTO.getCheckInDate();
        Timestamp newCheckOut = bookingRequestDTO.getCheckOutDate();
        BookingStatus newStatus = bookingRequestDTO.getBookingStatus();

        if (newStatus == BookingStatus.CANCELLED) {
            booking.setBookingStatus(BookingStatus.CANCELLED);
            booking.getRoom().setIsAvailable(true);
            roomRepository.save(booking.getRoom());
            bookingRepository.save(booking);
            return bookingMapper.toResponseDTO(booking);
        }

        validateBookingDates(newCheckIn, newCheckOut);
        validateNoOverlapForUpdate(booking, newCheckIn, newCheckOut);

        booking.setCheckInDate(newCheckIn);
        booking.setCheckOutDate(newCheckOut);
        booking.setBookingStatus(newStatus);
        booking.setTotalAmount(bookingRequestDTO.getTotalAmount());

        Booking updatedBooking = bookingRepository.save(booking);
        return bookingMapper.toResponseDTO(updatedBooking);
    }
    //check if guest exists
    private Guest validateGuestExists(String email) {
        Guest guest = guestRepository.findByEmail(email);
        if (guest == null) {
            logger.error("Guest not found with email: {}", email);
            throw new BookingNotFoundException("Guest not found with email: " + email);
        }
        return guest;
    }
    // check the booking dates
    private void validateBookingDates(Timestamp checkIn, Timestamp checkOut) {
        if (checkIn == null || checkOut == null || !checkIn.before(checkOut)) {
            throw new InvalidBookingDateException("Invalid check-in/check-out dates.");
        }
    }
    // find the available rooms
    private Room findAvailableRoom(List<Room> rooms, Timestamp checkIn, Timestamp checkOut) {
        for (Room room : rooms) {
            List<Booking> bookings = bookingRepository.findByRoom(room);
            boolean conflict = false;

            for (Booking booking : bookings) {
                if (booking.getBookingStatus() != BookingStatus.CANCELLED) {
                    boolean overlaps = checkIn.before(booking.getCheckOutDate()) && checkOut.after(booking.getCheckInDate());
                    if (overlaps) {
                        conflict = true;
                        break;
                    }
                }
            }
            if (!conflict) 
                return room;
            
        }
        throw new RoomAlreadyBookedException("No available rooms for the selected time range.");
    }

    private BigDecimal calculateTotalAmount(Room room, Timestamp checkIn, Timestamp checkOut) {
        if (room.getPricePerNight() == null) {
            throw new IllegalStateException("Room price is not set.");
        }
        long nights = (checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24);
        if (nights <= 0) {
            throw new InvalidBookingDateException("Invalid number of nights.");
        }
        return room.getPricePerNight().multiply(BigDecimal.valueOf(nights));
    }
    // validate room overlaps
    private void validateNoOverlapForUpdate(Booking booking, Timestamp checkIn, Timestamp checkOut) {
        List<Booking> roomBookings = bookingRepository.findByRoom(booking.getRoom());
        for (Booking other : roomBookings) {
            if (!other.getId().equals(booking.getId()) && other.getBookingStatus() != BookingStatus.CANCELLED) {
                boolean overlaps = checkIn.before(other.getCheckOutDate()) && checkOut.after(other.getCheckInDate());
                if (overlaps) {
                    throw new RoomAlreadyBookedException("Room already booked for the selected dates.");
                }
            }
        }
    }
    // generate transaction id
    private String generateTransactionId() {   
        int value=random.nextInt(9000) + 1000;
        return "TXN" + value;
    }
}
