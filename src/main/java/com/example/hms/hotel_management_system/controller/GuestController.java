package com.example.hms.hotel_management_system.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.example.hms.hotel_management_system.entity.Guest;
import com.example.hms.hotel_management_system.exception.GuestAlreadyExistsException;
import com.example.hms.hotel_management_system.exception.GuestNotFoundException;
import com.example.hms.hotel_management_system.service.GuestService;

@RestController
@RequestMapping("/guest")
public class GuestController {

    private static final Logger logger = LoggerFactory.getLogger(GuestController.class);

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @PostMapping("/add-guest")
    public Guest addGuest(@RequestBody Guest guest) {
        try {
            logger.info("Attempting to add guest: {}", guest.getEmail());
            return guestService.createGuest(guest);
        } catch (GuestAlreadyExistsException e) {
            logger.warn("Guest already exists: {}", guest.getEmail());
            return null;
        } catch (Exception e) {
            logger.error("Error while adding guest", e);
            return null;
        }
    }

    @GetMapping("/get-all")
    public List<Guest> getAllGuest() {
        try {
            logger.info("Fetching all guests...");
            return guestService.getAllGuest();
        } catch (GuestNotFoundException e) {
            logger.warn("No guests found: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("Error while fetching all guests", e);
            return null;
        }
    }

    @GetMapping("/get-by-email/{email}")
    public Guest getGuestById(@PathVariable String email) {
        try {
            logger.info("Fetching guest by email: {}", email);
            return guestService.getGuestByEmail(email);
        } catch (GuestNotFoundException e) {
            logger.warn("Guest not found with email: {}", email);
            return null;
        } catch (Exception e) {
            logger.error("Error while fetching guest by email", e);
            return null;
        }
    }

    @PutMapping("/update-by-email/{email}")
    public Guest updateGuestById(@RequestBody Guest guest, @PathVariable String email) {
        try {
            logger.info("Updating guest with email: {}", email);
            return guestService.updateGuestByEmail(guest, email);
        } catch (GuestNotFoundException e) {
            logger.warn("Guest not found for update with email: {}", email);
            return null;
        } catch (Exception e) {
            logger.error("Error while updating guest", e);
            return null;
        }
    }

    @DeleteMapping("/delete-by-email/{email}")
    public String deleteGuestByEmail(@PathVariable String email) {
        try {
            logger.info("Deleting guest with email: {}", email);
            guestService.deleteGuestByEmail(email);
            return "Guest deleted successfully...!";
        } catch (GuestNotFoundException e) {
            logger.warn("Guest not found for deletion with email: {}", email);
            return null;
        } catch (Exception e) {
            logger.error("Error while deleting guest", e);
            return null;
        }
    }
}
