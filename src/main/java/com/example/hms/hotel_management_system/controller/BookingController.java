package com.example.hms.hotel_management_system.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.example.hms.hotel_management_system.dto.BookingDTO;
import com.example.hms.hotel_management_system.exception.BookingNotFoundException;
import com.example.hms.hotel_management_system.exception.RoomAlreadyBookedException;
import com.example.hms.hotel_management_system.service.BookingService;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/add-booking")
    public BookingDTO addBooking(@RequestBody BookingDTO bookingDTO) {
        try {
            logger.info("Attempting to add booking: {}", bookingDTO);
            return bookingService.createBooking(bookingDTO);
        } catch (RoomAlreadyBookedException e) {
            logger.warn("Room already booked: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("Error occurred while adding booking", e);
            return null;
        }
    }

    @GetMapping("/get-all")
    public List<BookingDTO> getAllBookings() {
        try {
            logger.info("Fetching all bookings...");
            return bookingService.getAllBookings();
        } catch (BookingNotFoundException e) {
            logger.warn("No bookings found: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("Error occurred while fetching all bookings", e);
            return null;
        }
    }

    @PutMapping("/update-booking")
    public BookingDTO updateBooking(@RequestParam String roomNumber, @RequestParam String email,
            @RequestBody BookingDTO bookingDTO) {
        try {
            logger.info("Updating booking for room: {}, email: {}", roomNumber, email);
            return bookingService.updateBookingByRoomNumberAndEmail(roomNumber, email, bookingDTO);
        } catch (BookingNotFoundException e) {
            logger.warn("Booking not found for room: {} and email: {}", roomNumber, email);
            return null;
        } catch (Exception e) {
            logger.error("Error occurred while updating booking", e);
            return null;
        }
    }
}
