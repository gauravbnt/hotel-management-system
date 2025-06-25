package com.example.hms.hotel_management_system.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.hms.hotel_management_system.dto.request.BookingRequestDTO;
import com.example.hms.hotel_management_system.dto.response.BookingResponseDTO;
import com.example.hms.hotel_management_system.response.SuccessResponse;
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
    public ResponseEntity<SuccessResponse<BookingResponseDTO>> addBooking(@Valid @RequestBody BookingRequestDTO bookingRequestDTO) {
        logger.info("Attempting to add booking: {}", bookingRequestDTO);
        BookingResponseDTO booking = bookingService.createBooking(bookingRequestDTO);
    
        SuccessResponse<BookingResponseDTO> response = new SuccessResponse<>("Booking created successfully",HttpStatus.CREATED.value(),booking);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // get all bookings..
    @GetMapping("/get-all")
    public ResponseEntity<SuccessResponse<List<BookingResponseDTO>>> getAllBookings() {
        logger.info("Fetching all bookings...");
        List<BookingResponseDTO> bookingDTO = bookingService.getAllBookings();

        SuccessResponse<List<BookingResponseDTO>> response = new SuccessResponse<>("All bookings fetched successfully",HttpStatus.OK.value(), bookingDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // update the booking
    @PutMapping("/update-booking")
    public ResponseEntity<SuccessResponse<BookingResponseDTO>> updateBooking(@RequestParam String roomNumber,@RequestParam String email,@Valid @RequestBody BookingRequestDTO bookingDTO) {
        logger.info("Updating booking for room: {}, email: {}", roomNumber, email);
        BookingResponseDTO updatedBooking = bookingService.updateBookingByRoomNumberAndEmail(roomNumber, email,bookingDTO);
        
        SuccessResponse<BookingResponseDTO> response = new SuccessResponse<>("Booking updated successfully",HttpStatus.OK.value(),updatedBooking);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}