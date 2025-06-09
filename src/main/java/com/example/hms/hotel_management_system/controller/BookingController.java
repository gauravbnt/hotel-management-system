package com.example.hms.hotel_management_system.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.hms.hotel_management_system.dto.BookingRequestDTO;
import com.example.hms.hotel_management_system.dto.BookingResponseDTO;
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
    public ResponseEntity<ApiResponse<BookingResponseDTO>> addBooking(@RequestBody BookingRequestDTO bookingRequestDTO) {
        try {
            logger.info("Attempting to add booking: {}", bookingRequestDTO);
            BookingResponseDTO booking = bookingService.createBooking(bookingRequestDTO);

            ApiResponse<BookingResponseDTO> response = new ApiResponse<>(
                    "Booking created successfully",
                    HttpStatus.CREATED.value(),
                    booking);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RoomAlreadyBookedException e) {
            logger.warn("Room already booked: {}", e.getMessage());

            ApiResponse<BookingResponseDTO> response = new ApiResponse<>(
                    "Room already booked",
                    HttpStatus.CONFLICT.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } catch (Exception e) {
            logger.error("Error occurred while adding booking", e);

            ApiResponse<BookingResponseDTO> response = new ApiResponse<>(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get all bookings..
    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<BookingResponseDTO>>> getAllBookings() {
        try {
            logger.info("Fetching all bookings...");
            List<BookingResponseDTO> bookingDTO = bookingService.getAllBookings();

            ApiResponse<List<BookingResponseDTO>> response = new ApiResponse<>(
                    "All bookings fetched successfully",
                    HttpStatus.OK.value(),
                    bookingDTO);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BookingNotFoundException e) {
            logger.warn("No bookings found: {}", e.getMessage());

            ApiResponse<List<BookingResponseDTO>> response = new ApiResponse<>(
                    "No bookings found",
                    HttpStatus.NOT_FOUND.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error occurred while fetching all bookings", e);

            ApiResponse<List<BookingResponseDTO>> response = new ApiResponse<>(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // update the booking
    @PutMapping("/update-booking")
    public ResponseEntity<ApiResponse<BookingResponseDTO>> updateBooking(
            @RequestParam String roomNumber,
            @RequestParam String email,
            @RequestBody BookingRequestDTO bookingDTO) {
        try {
            logger.info("Updating booking for room: {}, email: {}", roomNumber, email);
            BookingResponseDTO updatedBooking = bookingService.updateBookingByRoomNumberAndEmail(roomNumber, email, bookingDTO);

            ApiResponse<BookingResponseDTO> response = new ApiResponse<>(
                    "Booking updated successfully",
                    HttpStatus.OK.value(),
                    updatedBooking);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BookingNotFoundException e) {
            logger.warn("Booking not found for room: {} and email: {}", roomNumber, email);

            ApiResponse<BookingResponseDTO> response = new ApiResponse<>(
                    "Booking not found",
                    HttpStatus.NOT_FOUND.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error occurred while updating booking", e);

            ApiResponse<BookingResponseDTO> response = new ApiResponse<>(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
