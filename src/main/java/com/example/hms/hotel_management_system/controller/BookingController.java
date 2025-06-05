package com.example.hms.hotel_management_system.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.hms.hotel_management_system.dto.BookingDTO;
import com.example.hms.hotel_management_system.exception.BookingNotFoundException;
import com.example.hms.hotel_management_system.exception.RoomAlreadyBookedException;
import com.example.hms.hotel_management_system.response.ApiResponse;
import com.example.hms.hotel_management_system.service.BookingService;

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
    public ResponseEntity<ApiResponse<BookingDTO>> addBooking(@RequestBody BookingDTO bookingDTO) {
        try {
            logger.info("Attempting to add booking: {}", bookingDTO);
            BookingDTO booking = bookingService.createBooking(bookingDTO);

            ApiResponse<BookingDTO> response = new ApiResponse<>(
                    "Booking created successfully",
                    HttpStatus.CREATED.value(),
                    booking);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RoomAlreadyBookedException e) {
            logger.warn("Room already booked: {}", e.getMessage());

            ApiResponse<BookingDTO> response = new ApiResponse<>(
                    "Room already booked",
                    HttpStatus.CONFLICT.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } catch (Exception e) {
            logger.error("Error occurred while adding booking", e);

            ApiResponse<BookingDTO> response = new ApiResponse<>(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get all bookings..
    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<BookingDTO>>> getAllBookings() {
        try {
            logger.info("Fetching all bookings...");
            List<BookingDTO> bookingDTOs = bookingService.getAllBookings();

            ApiResponse<List<BookingDTO>> response = new ApiResponse<>(
                    "All bookings fetched successfully",
                    HttpStatus.OK.value(),
                    bookingDTOs);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BookingNotFoundException e) {
            logger.warn("No bookings found: {}", e.getMessage());

            ApiResponse<List<BookingDTO>> response = new ApiResponse<>(
                    "No bookings found",
                    HttpStatus.NOT_FOUND.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error occurred while fetching all bookings", e);

            ApiResponse<List<BookingDTO>> response = new ApiResponse<>(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // update the booking
    @PutMapping("/update-booking")
    public ResponseEntity<ApiResponse<BookingDTO>> updateBooking(
            @RequestParam String roomNumber,
            @RequestParam String email,
            @RequestBody BookingDTO bookingDTO) {
        try {
            logger.info("Updating booking for room: {}, email: {}", roomNumber, email);
            BookingDTO updatedBooking = bookingService.updateBookingByRoomNumberAndEmail(roomNumber, email, bookingDTO);

            ApiResponse<BookingDTO> response = new ApiResponse<>(
                    "Booking updated successfully",
                    HttpStatus.OK.value(),
                    updatedBooking);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BookingNotFoundException e) {
            logger.warn("Booking not found for room: {} and email: {}", roomNumber, email);

            ApiResponse<BookingDTO> response = new ApiResponse<>(
                    "Booking not found",
                    HttpStatus.NOT_FOUND.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error occurred while updating booking", e);

            ApiResponse<BookingDTO> response = new ApiResponse<>(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
