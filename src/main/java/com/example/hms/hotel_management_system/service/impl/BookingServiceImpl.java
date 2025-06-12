package com.example.hms.hotel_management_system.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.hms.hotel_management_system.response.ErrorResponse;
import com.example.hms.hotel_management_system.response.SuccessResponse;
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

    
    
    @Override
    @Transactional
    public ResponseEntity<?> createBooking(BookingRequestDTO bookingDTO) {
        try {
        logger.info("Trying to create booking for email: {}", bookingDTO.getEmail());

        List<Room> allRooms = roomRepository.findByRoomTypeAndIsAvailableTrue(bookingDTO.getRoomType());

        Guest guest = guestRepository.findByEmail(bookingDTO.getEmail());

        Timestamp checkIn = bookingDTO.getCheckInDate();
        Timestamp checkOut = bookingDTO.getCheckOutDate();

        if (checkIn == null || checkOut == null || !checkIn.before(checkOut)) {
            throw new InvalidBookingDateException("Invalid check-in/check-out dates.");
        }

        if (allRooms == null || allRooms.isEmpty()) {
            throw new RoomAlreadyBookedException("No available rooms for type: " + bookingDTO.getRoomType());
        }

        if (guest == null) {
            throw new BookingNotFoundException("Guest not found with email: " + bookingDTO.getEmail());
        }

        Room selectedRoom = null;
        for (Room room : allRooms) {
            boolean conflict = bookingRepository.findByRoom(room).stream()
                .anyMatch(b -> b.getBookingStatus() != BookingStatus.CANCELLED &&
                        checkIn.before(b.getCheckOutDate()) &&
                        checkOut.after(b.getCheckInDate()));
            if (!conflict) {
                selectedRoom = room;
                break;
            }
        }

        if (selectedRoom == null) {
            throw new RoomAlreadyBookedException("No available rooms for the selected time range.");
        }

        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking.setRoom(selectedRoom);
        booking.setGuest(guest);
        roomRepository.save(selectedRoom);
        Booking savedBooking = bookingRepository.save(booking);

        PaymentRequestDTO payment = bookingDTO.getPayment();
        if (payment == null) {
            throw new PaymentInformationIsNullException("Payment information is required.");
        }

        long nights = (checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24);
        if (nights <= 0) {
            throw new InvalidBookingDateException("Invalid number of nights.");
        }

        if (selectedRoom.getPricePerNight() == null) {
            throw new IllegalStateException("Room price per night is not set.");
        }

        BigDecimal totalAmount = selectedRoom.getPricePerNight().multiply(BigDecimal.valueOf(nights));
        booking.setTotalAmount(totalAmount);
        payment.setAmountPaid(totalAmount);
        payment.setRoomNumber(selectedRoom.getRoomNumber());
        payment.setEmail(guest.getEmail());
        payment.setTransactionId(generateTransactionId());

        paymentService.createPayment(payment);

        BookingResponseDTO responseDTO = bookingMapper.toResponseDTO(savedBooking);
        return ResponseEntity.ok(new SuccessResponse<>("Booking successful", HttpStatus.OK.value(), responseDTO));
    }

    catch (InvalidBookingDateException | RoomAlreadyBookedException | BookingNotFoundException |
           PaymentInformationIsNullException | IllegalStateException e) {
        logger.error("Booking failed: {}", e.getMessage());
        return new ResponseEntity<>(
            new ErrorResponse("Booking Failed", HttpStatus.BAD_REQUEST.value()),
            HttpStatus.BAD_REQUEST
        );
    }

    catch (Exception e) {
        logger.error("Unexpected error during booking: {}", e.getMessage(), e);
        return new ResponseEntity<>(
            new ErrorResponse("Internal Server Error",HttpStatus.INTERNAL_SERVER_ERROR.value()),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
    

    @Override
    public ResponseEntity<?> getAllBookings() {
    try {
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

        SuccessResponse<List<BookingResponseDTO>> response = new SuccessResponse<>(
            "Bookings fetched successfully",
            HttpStatus.OK.value(),
            dtoList
        );
        return new ResponseEntity<>(response, HttpStatus.OK);

    } catch (BookingNotFoundException e) {
        logger.error("Error fetching bookings: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    } catch (Exception e) {
        logger.error("Unexpected error: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


    @Override
    public ResponseEntity<?> updateBookingByRoomNumberAndEmail(String roomNumber, String email, BookingRequestDTO bookingRequestDTO) {
    try {
        logger.info("Updating booking for room: {}, email: {}", roomNumber, email);
        Booking booking = bookingRepository.findByRoom_RoomNumberAndGuest_Email(roomNumber, email);

        if (booking == null) {
            logger.error("No booking found for room: {}, email: {}", roomNumber, email);
            throw new BookingNotFoundException("No booking found for room number: " + roomNumber + " and email: " + email);
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

            BookingResponseDTO responseDTO = bookingMapper.toResponseDTO(booking);
            SuccessResponse<BookingResponseDTO> successResponse = new SuccessResponse<>(
                "Booking cancelled successfully",
                HttpStatus.OK.value(),
                responseDTO
            );
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }

        if (newCheckIn == null || newCheckOut == null || !newCheckIn.before(newCheckOut)) {
            logger.error("Invalid updated dates: check-in={}, check-out={}", newCheckIn, newCheckOut);
            throw new InvalidBookingDateException("Invalid check-in/check-out dates.");
        }

        List<Booking> roomBookings = bookingRepository.findByRoom(booking.getRoom());
        for (Booking book : roomBookings) {
            if (!book.getId().equals(booking.getId()) && book.getBookingStatus() != BookingStatus.CANCELLED) {
                boolean overlaps = newCheckIn.before(book.getCheckOutDate()) && newCheckOut.after(book.getCheckInDate());
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

        BookingResponseDTO responseDTO = bookingMapper.toResponseDTO(updatedBooking);
        SuccessResponse<BookingResponseDTO> successResponse = new SuccessResponse<>(
            "Booking updated successfully",
            HttpStatus.OK.value(),
            responseDTO
        );
        return new ResponseEntity<>(successResponse, HttpStatus.OK);

    } catch (BookingNotFoundException | InvalidBookingDateException | RoomAlreadyBookedException e) {
        logger.error("Error updating booking: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
        logger.error("Unexpected error: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

}
