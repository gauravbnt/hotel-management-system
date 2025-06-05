package com.example.hms.hotel_management_system.service.Impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hms.hotel_management_system.entity.Guest;
import com.example.hms.hotel_management_system.exception.GuestAlreadyExistsException;
import com.example.hms.hotel_management_system.exception.GuestNotFoundException;
import com.example.hms.hotel_management_system.repository.GuestRepository;
import com.example.hms.hotel_management_system.service.GuestService;

@Service
public class GuestServiceImpl implements GuestService {

    private static final Logger logger = LoggerFactory.getLogger(GuestServiceImpl.class);

    private final GuestRepository guestRepository;

    public GuestServiceImpl(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    @Override
    public Guest createGuest(Guest guest) {
        logger.info("Attempting to create guest with email: {}", guest.getEmail());
        if (guestRepository.findByEmail(guest.getEmail()) != null) {
            logger.warn("Guest already exists with email: {}", guest.getEmail());
            throw new GuestAlreadyExistsException("Guest already exists with email: " + guest.getEmail());
        }
        Guest savedGuest = guestRepository.save(guest);
        logger.info("Guest created successfully with email: {}", savedGuest.getEmail());
        return savedGuest;
    }

    @Override
    public List<Guest> getAllGuest() {
        logger.info("Fetching all guests...");
        List<Guest> guests = guestRepository.findAll();
        if (guests.isEmpty()) {
            logger.warn("No guests found in the system.");
            throw new GuestNotFoundException("No guests found.");
        }
        logger.info("Found {} guests.", guests.size());
        return guests;
    }

    @Override
    public Guest getGuestByEmail(String email) {
        logger.info("Fetching guest by email: {}", email);
        Guest guest = guestRepository.findByEmail(email);
        if (guest == null) {
            logger.error("Guest not found with email: {}", email);
            throw new GuestNotFoundException("Guest not found with email: " + email);
        }
        logger.info("Guest found with email: {}", email);
        return guest;
    }

    @Override
    public Guest updateGuestByEmail(Guest updateGuest, String email) {
        logger.info("Attempting to update guest with email: {}", email);
        Guest guest = getGuestByEmail(email);
        if (guest == null) {
            logger.error("Cannot update. Guest not found with email: {}", email);
            throw new GuestNotFoundException("Cannot update. Guest not found with email: " + email);
        }
        guest.setAddress(updateGuest.getAddress());
        guest.setEmail(updateGuest.getEmail());
        guest.setFirstName(updateGuest.getFirstName());
        guest.setLastName(updateGuest.getLastName());
        guest.setPhoneNumber(updateGuest.getPhoneNumber());

        Guest updatedGuest = guestRepository.save(guest);
        logger.info("Guest updated successfully with email: {}", updatedGuest.getEmail());
        return updatedGuest;
    }

    @Transactional
    @Override
    public void deleteGuestByEmail(String email) {
        logger.info("Attempting to delete guest with email: {}", email);
        Guest guest = guestRepository.findByEmail(email);
        if (guest == null) {
            logger.error("Cannot delete. Guest not found with email: {}", email);
            throw new GuestNotFoundException("Cannot delete. Guest not found with email: " + email);
        }
        guestRepository.delete(guest);
        logger.info("Guest deleted successfully with email: {}", email);
    }
}
