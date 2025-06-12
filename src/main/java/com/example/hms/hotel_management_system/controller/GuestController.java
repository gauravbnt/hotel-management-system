package com.example.hms.hotel_management_system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.hms.hotel_management_system.dto.GuestRequestDTO;
import com.example.hms.hotel_management_system.response.SuccessResponse;
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
    public ResponseEntity<?> addGuest(@Valid @RequestBody GuestRequestDTO guest) {
            logger.info("Attempting to add guest: {}", guest.getEmail());
                return  guestService.createGuest(guest);
        
    }

    // getting all the guests
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllGuest() {   
            logger.info("Fetching all guests...");
            return guestService.getAllGuest();
        }

    // get the guest by email
    @GetMapping("/get-by-email/{email}")
    public ResponseEntity<?> getGuestById(@PathVariable String email) {

            logger.info("Fetching guest by email: {}", email);
                return guestService.getGuestByEmail(email);    }

    // update the guest by email
    @PutMapping("/update-by-email/{email}")
    public ResponseEntity<?> updateGuestById(@Valid @RequestBody GuestRequestDTO guest, @PathVariable String email) {
            logger.info("Updating guest with email: {}", email);
            return guestService.updateGuestByEmail(guest, email);

    }

    // delete the guest by email
    @DeleteMapping("/delete-by-email/{email}")
    public ResponseEntity<?> deleteGuestByEmail(@PathVariable String email) {
            logger.info("Deleting guest with email: {}", email);
            guestService.deleteGuestByEmail(email);
            SuccessResponse<String> response = new SuccessResponse<>(
                    "Guest deleted successfully",
                    HttpStatus.OK.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
