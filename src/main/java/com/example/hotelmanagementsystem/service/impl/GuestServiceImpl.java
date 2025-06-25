package com.example.hotelmanagementsystem.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hotelmanagementsystem.exception.GuestAlreadyExistsException;
import com.example.hotelmanagementsystem.exception.GuestNotFoundException;
import com.example.hotelmanagementsystem.dto.request.GuestRequestDTO;
import com.example.hotelmanagementsystem.dto.response.GuestResponseDTO;
import com.example.hotelmanagementsystem.entity.Guest;
import com.example.hotelmanagementsystem.mapper.GuestMapper;
import com.example.hotelmanagementsystem.repository.GuestRepository;
import com.example.hotelmanagementsystem.service.GuestService;

@Service
public class GuestServiceImpl implements GuestService {

    private static final Logger logger = LoggerFactory.getLogger(GuestServiceImpl.class);

    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    public GuestServiceImpl(GuestRepository guestRepository,
                            GuestMapper guestMapper) {
        this.guestRepository = guestRepository;
        this.guestMapper=guestMapper;
    }

    @Override
    public GuestResponseDTO createGuest(GuestRequestDTO guest) {
        logger.info("Attempting to create guest with email: {}", guest.getEmail());
        //check by using boolean method
        if (guestRepository.existsByEmail(guest.getEmail())) {
            logger.warn("Guest already exists with email: {}", guest.getEmail());
            throw new GuestAlreadyExistsException("Guest already exists with email: " + guest.getEmail());
        }
        Guest guestEntity= guestMapper.toEntity(guest);
        Guest savedGuest = guestRepository.save(guestEntity);
        logger.info("Guest created successfully with email: {}", savedGuest.getEmail());
        return guestMapper.toResponseDTO(savedGuest);
    }

    @Override
    public List<GuestResponseDTO> getAllGuest() {
        logger.info("Fetching all guests...");
        List<Guest> guests = guestRepository.findAll();
        if (guests.isEmpty()) {
            logger.warn("No guests found in the system.");
            throw new GuestNotFoundException("No guests found.");
        }

        List<GuestResponseDTO> guestDTO = guestMapper.toResponseDTOList(guests);

        logger.info("Found {} guests.", guests.size());
        return guestDTO;
    }

    @Override
    public GuestResponseDTO getGuestByEmail(String email) {
        logger.info("Fetching guest by email: {}", email);
        Guest guest = guestRepository.findByEmail(email);
        if (guest == null) {
            logger.error("Guest not found with email: {}", email);
            throw new GuestNotFoundException("Guest not found with email: " + email);
        }
        GuestResponseDTO guestResponseDTO=guestMapper.toResponseDTO(guest);
        logger.info("Guest found with email: {}", email);
        return guestResponseDTO;
    }

    @Override
    public GuestResponseDTO updateGuestByEmail(GuestRequestDTO guestRequestDTO, String email) {
        logger.info("Attempting to update guest with email: {}", email);
        Guest updateGuest=guestMapper.toEntity(guestRequestDTO);
        Guest guest = guestRepository.findByEmail(email);
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
        return guestMapper.toResponseDTO(updatedGuest);
    }

    @Transactional
    @Override
    public void deleteGuestByEmail(String email) {
        logger.info("Attempting to delete guest with email: {}", email);
        boolean guestExists=guestRepository.existsByEmail(email);
        if(!guestExists)
        {
            logger.error("Cannot delete. Guest not found with email: {}", email);
            throw new GuestNotFoundException("Cannot delete. Guest not found with email: " + email);
        }
        Guest guest=guestRepository.findByEmail(email);
        guestRepository.delete(guest);
        logger.info("Guest deleted successfully with email: {}", email);
    }
}
