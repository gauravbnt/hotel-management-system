package com.example.hms.hotel_management_system.service.Impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hms.hotel_management_system.exception.BookingNotFoundException;
import com.example.hms.hotel_management_system.exception.InvalidBookingDateException;
import com.example.hms.hotel_management_system.exception.PaymentInformationIsNullException;
import com.example.hms.hotel_management_system.exception.RoomAlreadyBookedException;
import com.example.hms.hotel_management_system.dto.BookingDTO;
import com.example.hms.hotel_management_system.dto.PaymentDTO;
import com.example.hms.hotel_management_system.entity.Booking;
import com.example.hms.hotel_management_system.entity.Guest;
import com.example.hms.hotel_management_system.entity.Room;
import com.example.hms.hotel_management_system.enums.BookingStatus;
import com.example.hms.hotel_management_system.mapper.BookingMapper;
import com.example.hms.hotel_management_system.repository.BookingRepository;
import com.example.hms.hotel_management_system.repository.GuestRepository;
import com.example.hms.hotel_management_system.repository.RoomRepository;
import com.example.hms.hotel_management_system.service.BookingService;
import com.example.hms.hotel_management_system.service.PaymentService;

@Service
public class BookingServiceImpl implements BookingService {

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

    private String generateTransactionId() {
        int random = new java.util.Random().nextInt(9000) + 1000;
        return "TXN" + random;
    }

    @Transactional
    // --create booking--//
    @Override
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        List<Room> allRooms = roomRepository.findAll(); // finding all rooms
        Guest guest = guestRepository.findByEmail(bookingDTO.getEmail());// finding the guest

        Timestamp checkIn = bookingDTO.getCheckInDate();
        Timestamp checkOut = bookingDTO.getCheckOutDate();

        if (checkIn == null || checkOut == null || !checkIn.before(checkOut)) {
            throw new InvalidBookingDateException("Invalid check-in/check-out dates.");
        }
        if (allRooms == null || allRooms.isEmpty()) {
            throw new RoomAlreadyBookedException("No rooms available.");
        }
        if (guest == null) {
            throw new BookingNotFoundException("Guest not found with email: " + bookingDTO.getEmail());
        }

        Room selectRoom = null;
        for (Room room : allRooms) {

            List<Booking> roomBookings = bookingRepository.findByRoom(room);// listing bookings by room

            boolean isConflict = false;
            for (Booking booking : roomBookings) {
                Timestamp existingCheckIn = booking.getCheckInDate();
                Timestamp existingCheckOut = booking.getCheckOutDate();

                if (booking.getBookingStatus() != BookingStatus.CANCELLED) {
                    boolean overlaps = checkIn.before(existingCheckOut) && checkOut.after(existingCheckIn);
                    if (overlaps) { // checking for overlaps
                        isConflict = true;
                        break;
                    }
                }
            }
            if (!isConflict) {
                selectRoom = room;
                break;
            }
        }

        if (selectRoom == null) {
            throw new RoomAlreadyBookedException("No available rooms for the selected time range.");
        }

        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking.setRoom(selectRoom);
        booking.setGuest(guest);

        selectRoom.setIsAvailable(false);
        roomRepository.save(selectRoom);

        Booking savedBooking = bookingRepository.save(booking);
        PaymentDTO paymentDTO = bookingDTO.getPaymentDTO();

        if (paymentDTO == null) {
            throw new PaymentInformationIsNullException("Payment information is required for booking.");
        }
        // check for no of days booking..
        long nights = (checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24);
        System.out.println("No of nights " + nights);
        if (nights <= 0) {
            throw new InvalidBookingDateException("Check-in and check-out dates are invalid.");
        }

        BigDecimal totalAmount = selectRoom.getPricePerNight().multiply(BigDecimal.valueOf(nights));

        paymentDTO.setRoomNumber(savedBooking.getRoom().getRoomNumber());
        paymentDTO.setEmail(savedBooking.getGuest().getEmail());
        booking.setTotalAmount(totalAmount);
        if (selectRoom.getPricePerNight() == null) {
            throw new IllegalStateException("Room price is not set.");
        }

        if (totalAmount == null) {
            throw new IllegalArgumentException("Amount paid is missing.");
        }

        paymentDTO.setAmountPaid(totalAmount);
        paymentDTO.setTransactionId(generateTransactionId());
        paymentService.createPayment(paymentDTO);

        return bookingMapper.toDTO(savedBooking);
    }

    @Override
    public List<BookingDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        if (bookings.isEmpty()) {
            throw new BookingNotFoundException("No bookings found.");
        }
        List<BookingDTO> dtoList = new ArrayList<>();
        for (Booking booking : bookings) {
            dtoList.add(bookingMapper.toDTO(booking));
        }
        return dtoList;
    }

    @Override
    public BookingDTO updateBookingByRoomNumberAndEmail(String roomNumber, String email, BookingDTO bookingDTO) {
        Booking booking = bookingRepository.findByRoom_RoomNumberAndGuest_Email(roomNumber, email);

        if (booking == null) {
            throw new BookingNotFoundException(
                    "No booking found for room number: " + roomNumber + " and email: " + email);
        }

        Timestamp newCheckIn = bookingDTO.getCheckInDate();
        Timestamp newCheckOut = bookingDTO.getCheckOutDate();
        BookingStatus newStatus = bookingDTO.getBookingStatus();

        if (newStatus == BookingStatus.CANCELLED) {
            booking.setBookingStatus(BookingStatus.CANCELLED);
            booking.getRoom().setIsAvailable(true);
            roomRepository.save(booking.getRoom());
            bookingRepository.save(booking);
            return bookingMapper.toDTO(booking);
        }
        if (newCheckIn == null || newCheckOut == null || !newCheckIn.before(newCheckOut)) {
            throw new InvalidBookingDateException("Invalid check-in/check-out dates.");
        }

        List<Booking> roomBookings = bookingRepository.findByRoom(booking.getRoom());
        for (Booking book : roomBookings) {
            if (!book.getId().equals(booking.getId()) && book.getBookingStatus() != BookingStatus.CANCELLED) {
                boolean overlaps = newCheckIn.before(book.getCheckOutDate())
                        && newCheckOut.after(book.getCheckInDate());
                if (overlaps) {
                    throw new RoomAlreadyBookedException("Room already booked for the selected dates.");
                }
            }
        }
        booking.setCheckInDate(newCheckIn);
        booking.setCheckOutDate(newCheckOut);
        booking.setBookingStatus(newStatus);
        booking.setTotalAmount(bookingDTO.getTotalAmount());

        Booking updatedBooking = bookingRepository.save(booking);
        return bookingMapper.toDTO(updatedBooking);
    }
}
