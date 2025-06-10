package com.example.hms.hotel_management_system.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.hms.hotel_management_system.dto.GuestRequestDTO;
import com.example.hms.hotel_management_system.dto.GuestResponseDTO;
import com.example.hms.hotel_management_system.exception.GuestAlreadyExistsException;
import com.example.hms.hotel_management_system.exception.GuestNotFoundException;
import com.example.hms.hotel_management_system.response.ApiResponse;
import com.example.hms.hotel_management_system.service.GuestService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/guest")
public class GuestController {
    // Logger insertion
    private static final Logger logger = LoggerFactory.getLogger(GuestController.class);

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    // adding a guest
    @PostMapping("/add-guest")
    public ResponseEntity<ApiResponse<GuestResponseDTO>> addGuest(@Valid @RequestBody GuestRequestDTO guest) {
        try {
            logger.info("Attempting to add guest: {}", guest.getEmail());
            GuestResponseDTO guest_ob = guestService.createGuest(guest);
            ApiResponse<GuestResponseDTO> response = new ApiResponse<>(
                    "Guest added successfully",
                    HttpStatus.CREATED.value(),
                    guest_ob);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (GuestAlreadyExistsException e) {
            logger.warn("Guest already exists: {}", guest.getEmail());
            ApiResponse<GuestResponseDTO> response = new ApiResponse<>(
                    "Guest already exists",
                    HttpStatus.CONFLICT.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.CONFLICT);

        } catch (Exception e) {
            logger.error("Error while adding guest", e);
            ApiResponse<GuestResponseDTO> response = new ApiResponse<>(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // getting all the guests
    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<GuestResponseDTO>>> getAllGuest() {
        try {
            logger.info("Fetching all guests...");
            List<GuestResponseDTO> guests = guestService.getAllGuest();

            ApiResponse<List<GuestResponseDTO>> response = new ApiResponse<>(
                    "Guests fetched successfully",
                    HttpStatus.OK.value(),
                    guests);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (GuestNotFoundException e) {
            logger.warn("No guests found: {}", e.getMessage());

            ApiResponse<List<GuestResponseDTO>> response = new ApiResponse<>(
                    "No guests found",
                    HttpStatus.NOT_FOUND.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            logger.error("Error while fetching all guests", e);

            ApiResponse<List<GuestResponseDTO>> response = new ApiResponse<>(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get the guest by email
    @GetMapping("/get-by-email/{email}")
    public ResponseEntity<ApiResponse<GuestResponseDTO>> getGuestById(@PathVariable String email) {
        try {
            logger.info("Fetching guest by email: {}", email);
            GuestResponseDTO guest = guestService.getGuestByEmail(email);

            ApiResponse<GuestResponseDTO> response = new ApiResponse<>(
                    "Guest fetched successfully",
                    HttpStatus.OK.value(),
                    guest);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (GuestNotFoundException e) {
            logger.warn("Guest not found with email: {}", email);

            ApiResponse<GuestResponseDTO> response = new ApiResponse<>(
                    "Guest not found",
                    HttpStatus.NOT_FOUND.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            logger.error("Error while fetching guest by email", e);

            ApiResponse<GuestResponseDTO> response = new ApiResponse<>(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // update the guest by email
    @PutMapping("/update-by-email/{email}")
    public ResponseEntity<ApiResponse<GuestResponseDTO>> updateGuestById(@Valid @RequestBody GuestRequestDTO guest, @PathVariable String email) {
        try {
            logger.info("Updating guest with email: {}", email);
            GuestResponseDTO updatedGuest = guestService.updateGuestByEmail(guest, email);

            ApiResponse<GuestResponseDTO> response = new ApiResponse<>(
                    "Guest updated successfully",
                    HttpStatus.OK.value(),
                    updatedGuest);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (GuestNotFoundException e) {
            logger.warn("Guest not found for update with email: {}", email);

            ApiResponse<GuestResponseDTO> response = new ApiResponse<>(
                    "Guest not found",
                    HttpStatus.NOT_FOUND.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            logger.error("Error while updating guest", e);

            ApiResponse<GuestResponseDTO> response = new ApiResponse<>(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // delete the guest by email
    @DeleteMapping("/delete-by-email/{email}")
    public ResponseEntity<ApiResponse<String>> deleteGuestByEmail(@PathVariable String email) {
        try {
            logger.info("Deleting guest with email: {}", email);
            guestService.deleteGuestByEmail(email);

            ApiResponse<String> response = new ApiResponse<>(
                    "Guest deleted successfully",
                    HttpStatus.OK.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (GuestNotFoundException e) {
            logger.warn("Guest not found for deletion with email: {}", email);

            ApiResponse<String> response = new ApiResponse<>(
                    "Guest not found",
                    HttpStatus.NOT_FOUND.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            logger.error("Error while deleting guest", e);

            ApiResponse<String> response = new ApiResponse<>(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
