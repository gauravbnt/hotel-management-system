package com.example.hms.hotel_management_system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hms.hotel_management_system.dto.request.GuestRequestDTO;
import com.example.hms.hotel_management_system.dto.response.GuestResponseDTO;
import com.example.hms.hotel_management_system.entity.Guest;
import com.example.hms.hotel_management_system.exception.GuestAlreadyExistsException;
import com.example.hms.hotel_management_system.exception.GuestNotFoundException;
import com.example.hms.hotel_management_system.mapper.GuestMapper;
import com.example.hms.hotel_management_system.repository.GuestRepository;
import com.example.hms.hotel_management_system.response.ErrorResponse;
import com.example.hms.hotel_management_system.response.SuccessResponse;
import com.example.hms.hotel_management_system.service.GuestService;

@Service
public class GuestServiceImpl implements GuestService {

    private static final Logger logger = LoggerFactory.getLogger(GuestServiceImpl.class);

    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    public GuestServiceImpl(GuestRepository guestRepository,
            GuestMapper guestMapper) {
        this.guestRepository = guestRepository;
        this.guestMapper = guestMapper;
    }

    @Override
    public ResponseEntity<?> createGuest(GuestRequestDTO guest) {
        try {
            logger.info("Attempting to create guest with email: {}", guest.getEmail());
            if (guestRepository.findByEmail(guest.getEmail()) != null) {
                logger.warn("Guest already exists with email: {}", guest.getEmail());
                throw new GuestAlreadyExistsException("Guest already exists with email: " + guest.getEmail());
            }
            Guest guestEntity = guestMapper.toEntity(guest);
            Guest savedGuest = guestRepository.save(guestEntity);
            logger.info("Guest created successfully with email: {}", savedGuest.getEmail());

            GuestResponseDTO responseDTO = guestMapper.toResponseDTO(savedGuest);
            SuccessResponse<GuestResponseDTO> successResponse = new SuccessResponse<>(
                    "Guest created successfully",
                    HttpStatus.OK.value(),
                    responseDTO);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (GuestAlreadyExistsException e) {
            logger.error("Error creating guest: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getAllGuest() {
        try {
            logger.info("Fetching all guests...");
            List<Guest> guests = guestRepository.findAll();
            if (guests.isEmpty()) {
                logger.warn("No guests found in the system.");
                throw new GuestNotFoundException("No guests found.");
            }

            List<GuestResponseDTO> dtoList = new ArrayList<>();
            for (Guest guest : guests) {
                dtoList.add(guestMapper.toResponseDTO(guest));
            }

            logger.info("Found {} guests.", guests.size());
            SuccessResponse<List<GuestResponseDTO>> response = new SuccessResponse<>(
                    "Guests fetched successfully",
                    HttpStatus.OK.value(),
                    dtoList);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (GuestNotFoundException e) {
            logger.error("No guest found: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getGuestByEmail(String email) {
        try {
            logger.info("Fetching guest by email: {}", email);
            Guest guest = guestRepository.findByEmail(email);
            if (guest == null) {
                logger.error("Guest not found with email: {}", email);
                throw new GuestNotFoundException("Guest not found with email: " + email);
            }
            logger.info("Guest found with email: {}", email);
            GuestResponseDTO toResponseDTO = guestMapper.toResponseDTO(guest);
            SuccessResponse<GuestResponseDTO> response = new SuccessResponse<>(
                    "Guests fetched successfully",
                    HttpStatus.OK.value(),
                    toResponseDTO);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (GuestNotFoundException e) {
            logger.error("No guest found: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> updateGuestByEmail(GuestRequestDTO guestRequestDTO, String email) {
        try {
            logger.info("Attempting to update guest with email: {}", email);
            Guest updateGuest = guestMapper.toEntity(guestRequestDTO);
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
            GuestResponseDTO responseDTO = guestMapper.toResponseDTO(updatedGuest);
            SuccessResponse<GuestResponseDTO> response = new SuccessResponse<>(
                    "Guests updated successfully",
                    HttpStatus.OK.value(),
                    responseDTO);

            logger.info("Guest updated successfully with email: {}", updatedGuest.getEmail());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (GuestNotFoundException e) {
            logger.error("No guest found: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<?> deleteGuestByEmail(String email) {
        try {
            logger.info("Attempting to delete guest with email: {}", email);
            Guest guest = guestRepository.findByEmail(email);
            if (guest == null) {
                logger.error("Cannot delete. Guest not found with email: {}", email);
                throw new GuestNotFoundException("Cannot delete. Guest not found with email: " + email);
            }
            guestRepository.delete(guest);
            logger.info("Guest deleted successfully with email: {}", email);
            return new ResponseEntity<>(
                    new SuccessResponse<>("Guest deleted successfully", HttpStatus.OK.value(), null),
                    HttpStatus.OK);
        } catch (GuestNotFoundException e) {
            logger.error("No guest found: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
