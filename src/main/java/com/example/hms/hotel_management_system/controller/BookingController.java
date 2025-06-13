package com.example.hms.hotel_management_system.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.hms.hotel_management_system.dto.request.BookingRequestDTO;
import com.example.hms.hotel_management_system.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // add booking
        @PostMapping("/add-booking")
        public ResponseEntity<?> addBooking(@Valid @RequestBody BookingRequestDTO bookingRequestDTO) {
             logger.info("Attempting to add booking: {}", bookingRequestDTO);
             return bookingService.createBooking(bookingRequestDTO); 
        }


    // get all bookings..
        @GetMapping("/get-all")
        public ResponseEntity<?> getAllBookings() {
            logger.info("Fetching all bookings...");
             return bookingService.getAllBookings();     
    }

    // update the booking
        @PutMapping("/update-booking")
        public ResponseEntity<?> updateBooking(
                @RequestParam String roomNumber,
                @RequestParam String email,
                @Valid @RequestBody BookingRequestDTO bookingDTO) {
            logger.info("Updating booking for room: {}, email: {}", roomNumber, email);
            return bookingService.updateBookingByRoomNumberAndEmail(roomNumber, email, bookingDTO);
    }
}
