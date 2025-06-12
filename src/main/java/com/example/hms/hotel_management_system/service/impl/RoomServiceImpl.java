package com.example.hms.hotel_management_system.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.hms.hotel_management_system.dto.RoomRequestDTO;
import com.example.hms.hotel_management_system.dto.RoomResponseDTO;
import com.example.hms.hotel_management_system.entity.Room;
import com.example.hms.hotel_management_system.exception.RoomAlreadyExistsException;
import com.example.hms.hotel_management_system.exception.RoomNotFoundException;
import com.example.hms.hotel_management_system.mapper.RoomMapper;
import com.example.hms.hotel_management_system.repository.RoomRepository;
import com.example.hms.hotel_management_system.response.ErrorResponse;
import com.example.hms.hotel_management_system.response.SuccessResponse;
import com.example.hms.hotel_management_system.service.RoomService;

@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public RoomServiceImpl(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    @Override
    public ResponseEntity<?> createRoom(RoomRequestDTO roomRequestDTO) {
        try {
            logger.info("Creating room with room number: {}", roomRequestDTO.getRoomNumber());
            if (roomRepository.findRoomByRoomNumber(roomRequestDTO.getRoomNumber()) != null) {
                logger.warn("Room already exists with number: {}", roomRequestDTO.getRoomNumber());
                throw new RoomAlreadyExistsException(
                        "Room already exists with number: " + roomRequestDTO.getRoomNumber());
            }
            Room room = roomMapper.toEntity(roomRequestDTO);
            Room savedRoom = roomRepository.save(room);
            logger.info("Room created successfully: {}", savedRoom.getRoomNumber());
            RoomResponseDTO responseDTO = roomMapper.toResponseDTO(savedRoom);
            SuccessResponse<RoomResponseDTO> response = new SuccessResponse<>(
                    "Room created successfully",
                    HttpStatus.OK.value(),
                    responseDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RoomAlreadyExistsException e) {
            logger.error("Room Already exists : {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Room Already exists", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getRoomByRoomNumber(String roomNumber) {
        try {
            logger.info("Retrieving room with room number: {}", roomNumber);
            Room room = roomRepository.findRoomByRoomNumber(roomNumber);
            if (room == null) {
                logger.error("Room not found with number: {}", roomNumber);
                throw new RoomNotFoundException("Room not found with number: " + roomNumber);
            }
            RoomResponseDTO responseDTO = roomMapper.toResponseDTO(room);
            SuccessResponse<RoomResponseDTO> response = new SuccessResponse<>(
                    "Room fetched successfully",
                    HttpStatus.OK.value(),
                    responseDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RoomNotFoundException e) {
            logger.error("Room not found: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Room not found", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getAvailableRooms(Boolean isAvailable) {
        try {
            logger.info("Retrieving available rooms with availability: {}", isAvailable);
            if (isAvailable == null) {
                logger.error("Availability flag is null.");
                throw new IllegalArgumentException("Availability flag must not be null.");
            }
            List<Room> room = roomRepository.findByIsAvailable(isAvailable);
            List<RoomResponseDTO> responseDTO = roomMapper.toResponseDTO(room);
            SuccessResponse<List<RoomResponseDTO>> response = new SuccessResponse<>(
                    "Room fetched successfully",
                    HttpStatus.OK.value(),
                    responseDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RoomNotFoundException e) {
            logger.error("Room not found: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Rooms not found", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getAllRooms() {
        try {
            logger.info("Retrieving all rooms");
            List<Room> rooms = roomRepository.findAll();
            if (rooms.isEmpty()) {
                logger.warn("No rooms found.");
                throw new RoomNotFoundException("No rooms found.....");
            }
            List<RoomResponseDTO> responseDTO = roomMapper.toResponseDTO(rooms);
            SuccessResponse<List<RoomResponseDTO>> response = new SuccessResponse<>(
                    "Room fetched successfully",
                    HttpStatus.OK.value(),
                    responseDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RoomNotFoundException e) {
            logger.error("Room not found: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Rooms not found", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> updateRoomByRoomNumber(RoomRequestDTO roomRequestDTO, String roomNumber) {
        try {
            logger.info("Updating room with room number: {}", roomNumber);
            if (roomRequestDTO == null) {
                logger.error("Room update data is null for room number: {}", roomNumber);
                throw new RoomNotFoundException("Room not found with number: " + roomNumber);
            }

            Room r1 = roomRepository.findRoomByRoomNumber(roomNumber);
            r1.setRoomNumber(roomRequestDTO.getRoomNumber());
            r1.setRoomType(roomRequestDTO.getRoomType());
            r1.setFloorNumber(roomRequestDTO.getFloorNumber());
            r1.setDescription(roomRequestDTO.getDescription());
            r1.setIsAvailable(roomRequestDTO.getIsAvailable());
            r1.setPricePerNight(roomRequestDTO.getPricePerNight());

            Room room = roomRepository.save(r1);

            logger.info("Room updated successfully: {}", room.getRoomNumber());
            RoomResponseDTO responseDTO = roomMapper.toResponseDTO(room);
            SuccessResponse<RoomResponseDTO> response = new SuccessResponse<>(
                    "Room updated successfully",
                    HttpStatus.OK.value(),
                    responseDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RoomNotFoundException e) {
            logger.error("Room not found : {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Room not found", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
