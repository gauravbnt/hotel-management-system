package com.example.hms.hotel_management_system.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hms.hotel_management_system.exception.BookingNotFoundException;
import com.example.hms.hotel_management_system.exception.InvalidBookingDateException;
import com.example.hms.hotel_management_system.exception.PaymentInformationIsNullException;
import com.example.hms.hotel_management_system.exception.RoomAlreadyBookedException;
import com.example.hms.hotel_management_system.dto.BookingRequestDTO;
import com.example.hms.hotel_management_system.dto.BookingResponseDTO;
import com.example.hms.hotel_management_system.dto.PaymentRequestDTO;
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

    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

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
    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO bookingDTO) {
        logger.info("Trying to create booking for email: {}", bookingDTO.getEmail());

        List<Room> allRooms = roomRepository.findByRoomTypeAndIsAvailableTrue(bookingDTO.getRoomType());
        logger.debug("Available rooms of type {}: {}", bookingDTO.getRoomType(), allRooms.size());

        
        Guest guest = guestRepository.findByEmail(bookingDTO.getEmail());
        logger.debug("Guest lookup for email {}: {}", bookingDTO.getEmail(), guest != null ? "Found" : "Not Found");
        
        Timestamp checkIn = bookingDTO.getCheckInDate();
        Timestamp checkOut = bookingDTO.getCheckOutDate();

        if (checkIn == null || checkOut == null || !checkIn.before(checkOut)) {
            logger.error("Invalid Dates Entered: check-in={}, check-out={}", checkIn, checkOut);
            throw new InvalidBookingDateException("Invalid check-in/check-out dates.");
        }

        if (allRooms == null || allRooms.isEmpty()) {
            logger.error("No available rooms for type: {}", bookingDTO.getRoomType());
            throw new RoomAlreadyBookedException("No available rooms for type: " + bookingDTO.getRoomType());
        }

        if (guest == null) {
            logger.error("Guest not found with email: {}", bookingDTO.getEmail());
            throw new BookingNotFoundException("Guest not found with email: " + bookingDTO.getEmail());
        }

        Room selectRoom = null;
        for (Room room : allRooms) {
            List<Booking> roomBookings = bookingRepository.findByRoom(room);
            boolean isConflict = false;

            for (Booking booking : roomBookings) {
                Timestamp existingCheckIn = booking.getCheckInDate();
                Timestamp existingCheckOut = booking.getCheckOutDate();

                if (booking.getBookingStatus() != BookingStatus.CANCELLED) {
                    boolean overlaps = checkIn.before(existingCheckOut) && checkOut.after(existingCheckIn);
                    if (overlaps) {
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
            logger.warn("No available room for the selected time range.");
            throw new RoomAlreadyBookedException("No available rooms for the selected time range.");
        }
        logger.info("Room selected: {}", selectRoom.getRoomNumber());

        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking.setRoom(selectRoom);
        booking.setGuest(guest);

        //selectRoom.setIsAvailable(false);
        roomRepository.save(selectRoom);

        Booking savedBooking = bookingRepository.save(booking);
        PaymentRequestDTO payment = bookingDTO.getPayment();

        if (payment == null) {
            logger.error("Payment information is missing.");
            throw new PaymentInformationIsNullException("Payment information is required for booking.");
        }

        long nights = (checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24);
        logger.debug("Number of nights calculated: {}", nights);

        if (nights <= 0) {
            logger.error("Invalid booking duration (<= 0 nights).");
            throw new InvalidBookingDateException("Check-in and check-out dates are invalid.");
        }

        BigDecimal totalAmount = selectRoom.getPricePerNight().multiply(BigDecimal.valueOf(nights));
        logger.info("Total amount calculated: {}", totalAmount);

        if (selectRoom.getPricePerNight() == null) {
            logger.error("Room price per night is not set for room {}", selectRoom.getRoomNumber());
            throw new IllegalStateException("Room price is not set.");
        }

        if (totalAmount == null) {
            logger.error("Total amount is null.");
            throw new IllegalArgumentException("Amount paid is missing.");
        }

        payment.setRoomNumber(savedBooking.getRoom().getRoomNumber());
        payment.setEmail(savedBooking.getGuest().getEmail());
        booking.setTotalAmount(totalAmount);

        payment.setAmountPaid(totalAmount);
        String txnId = generateTransactionId();
        payment.setTransactionId(txnId);
        logger.info("Processing payment with transaction ID: {}", txnId);

        paymentService.createPayment(payment);

        logger.info("Booking successfully created for room {} and guest {}", selectRoom.getRoomNumber(),
                guest.getEmail());

        return bookingMapper.toResponseDTO(savedBooking);
    }

    @Override
    public List<BookingResponseDTO> getAllBookings() {
        logger.info("Fetching all bookings...");
        List<Booking> bookings = bookingRepository.findAll();
        if (bookings.isEmpty()) {
            logger.warn("No bookings found.");
            throw new BookingNotFoundException("No bookings found.");
        }

        logger.debug("Number of bookings fetched: {}", bookings.size());
        List<BookingResponseDTO> dtoList = new ArrayList<>();
        for (Booking booking : bookings) {
            dtoList.add(bookingMapper.toResponseDTO(booking));
        }
        return dtoList;
    }

    @Override
    public BookingResponseDTO updateBookingByRoomNumberAndEmail(String roomNumber, String email, BookingRequestDTO bookingRequestDTO) {
        logger.info("Updating booking for room: {}, email: {}", roomNumber, email);
        Booking booking = bookingRepository.findByRoom_RoomNumberAndGuest_Email(roomNumber, email);

        if (booking == null) {
            logger.error("No booking found for room: {}, email: {}", roomNumber, email);
            throw new BookingNotFoundException(
                "No booking found for room number: " + roomNumber + " and email: " + email);
        }

        Timestamp newCheckIn = bookingRequestDTO.getCheckInDate();
        Timestamp newCheckOut = bookingRequestDTO.getCheckOutDate();
        BookingStatus newStatus = bookingRequestDTO.getBookingStatus();

        if (newStatus == BookingStatus.CANCELLED) {
            logger.info("Cancelling booking for room: {}", roomNumber);
            booking.setBookingStatus(BookingStatus.CANCELLED);
            booking.getRoom().setIsAvailable(true);
            roomRepository.save(booking.getRoom());
            bookingRepository.save(booking);
            return bookingMapper.toResponseDTO(booking);
        }

        if (newCheckIn == null || newCheckOut == null || !newCheckIn.before(newCheckOut)) {
            logger.error("Invalid updated dates: check-in={}, check-out={}", newCheckIn, newCheckOut);
            throw new InvalidBookingDateException("Invalid check-in/check-out dates.");
        }

        List<Booking> roomBookings = bookingRepository.findByRoom(booking.getRoom());
        for (Booking book : roomBookings) {
            if (!book.getId().equals(booking.getId()) && book.getBookingStatus() != BookingStatus.CANCELLED) {
                boolean overlaps = newCheckIn.before(book.getCheckOutDate())
                        && newCheckOut.after(book.getCheckInDate());
                if (overlaps) {
                    logger.warn("Booking conflict detected during update.");
                    throw new RoomAlreadyBookedException("Room already booked for the selected dates.");
                }
            }
        }
        booking.setCheckInDate(newCheckIn);
        booking.setCheckOutDate(newCheckOut);
        booking.setBookingStatus(newStatus);
        booking.setTotalAmount(bookingRequestDTO.getTotalAmount());

        Booking updatedBooking = bookingRepository.save(booking);
        logger.info("Booking updated successfully for room: {}", roomNumber);
        return bookingMapper.toResponseDTO(updatedBooking);

    }
}
