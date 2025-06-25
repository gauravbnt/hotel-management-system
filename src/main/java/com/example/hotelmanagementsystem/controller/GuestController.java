package com.example.hotelmanagementsystem.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.hotelmanagementsystem.dto.request.GuestRequestDTO;
import com.example.hotelmanagementsystem.dto.response.GuestResponseDTO;
import com.example.hotelmanagementsystem.response.SuccessResponse;
import com.example.hotelmanagementsystem.service.GuestService;

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
    public ResponseEntity<SuccessResponse<GuestResponseDTO>> addGuest(@Valid @RequestBody GuestRequestDTO guest) {
        logger.info("Attempting to add guest: {}", guest.getEmail());
        GuestResponseDTO guestResponseDTO = guestService.createGuest(guest);

        SuccessResponse<GuestResponseDTO> response = new SuccessResponse<>("Guest added successfully",HttpStatus.CREATED.value(),guestResponseDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // getting all the guests
    @GetMapping("/get-all")
    public ResponseEntity<SuccessResponse<List<GuestResponseDTO>>> getAllGuest() {
        logger.info("Fetching all guests...");
        List<GuestResponseDTO> guests = guestService.getAllGuest();

        SuccessResponse<List<GuestResponseDTO>> response = new SuccessResponse<>("Guests fetched successfully",HttpStatus.OK.value(),guests);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // get the guest by email
    @GetMapping("/get-by-email/{email}")
    public ResponseEntity<SuccessResponse<GuestResponseDTO>> getGuestById(@PathVariable String email) {
        logger.info("Fetching guest by email: {}", email);
        GuestResponseDTO guest = guestService.getGuestByEmail(email);

        SuccessResponse<GuestResponseDTO> response = new SuccessResponse<>("Guest fetched successfully",HttpStatus.OK.value(),guest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // update the guest by email
    @PutMapping("/update-by-email/{email}")
    public ResponseEntity<SuccessResponse<GuestResponseDTO>> updateGuestById(@Valid @RequestBody GuestRequestDTO guest,@PathVariable String email) {
        logger.info("Updating guest with email: {}", email);
        GuestResponseDTO updatedGuest = guestService.updateGuestByEmail(guest, email);

        SuccessResponse<GuestResponseDTO> response = new SuccessResponse<>("Guest updated successfully",HttpStatus.OK.value(),updatedGuest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // delete the guest by email
    @DeleteMapping("/delete-by-email/{email}")
    public ResponseEntity<SuccessResponse<String>> deleteGuestByEmail(@PathVariable String email) {
        logger.info("Deleting guest with email: {}", email);
        guestService.deleteGuestByEmail(email);

        SuccessResponse<String> response = new SuccessResponse<>("Guest deleted successfully",HttpStatus.OK.value(),null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
