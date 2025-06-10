package com.example.hms.hotel_management_system.service.Impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.hms.hotel_management_system.dto.RoomRequestDTO;
import com.example.hms.hotel_management_system.dto.RoomResponseDTO;
import com.example.hms.hotel_management_system.entity.Room;
import com.example.hms.hotel_management_system.exception.RoomAlreadyExistsException;
import com.example.hms.hotel_management_system.exception.RoomNotFoundException;
import com.example.hms.hotel_management_system.mapper.RoomMapper;
import com.example.hms.hotel_management_system.repository.RoomRepository;
import com.example.hms.hotel_management_system.service.RoomService;

@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public RoomServiceImpl(RoomRepository roomRepository,RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper=roomMapper;
    }

    @Override
    public RoomResponseDTO createRoom(RoomRequestDTO roomRequestDTO) {
        logger.info("Creating room with room number: {}", roomRequestDTO.getRoomNumber());
        if (roomRepository.findRoomByRoomNumber(roomRequestDTO.getRoomNumber()) != null) {
            logger.warn("Room already exists with number: {}", roomRequestDTO.getRoomNumber());
            throw new RoomAlreadyExistsException("Room already exists with number: " + roomRequestDTO.getRoomNumber());
        }
        Room room=roomMapper.toEntity(roomRequestDTO);
        Room savedRoom = roomRepository.save(room);
        logger.info("Room created successfully: {}", savedRoom.getRoomNumber());
        return roomMapper.toResponseDTO(savedRoom);
    }

    @Override
    public RoomResponseDTO getRoomByRoomNumber(String roomNumber) {
        logger.info("Retrieving room with room number: {}", roomNumber);
        Room room = roomRepository.findRoomByRoomNumber(roomNumber);
        if (room == null) {
            logger.error("Room not found with number: {}", roomNumber);
            throw new RoomNotFoundException("Room not found with number: " + roomNumber);
        }
        return roomMapper.toResponseDTO(room);
    }

    @Override
    public List<RoomResponseDTO> getAvailableRooms(Boolean isAvailable) {
        logger.info("Retrieving available rooms with availability: {}", isAvailable);
        if (isAvailable == null) {
            logger.error("Availability flag is null.");
            throw new IllegalArgumentException("Availability flag must not be null.");
        }
        List<Room> room=roomRepository.findByIsAvailable(isAvailable);
        return roomMapper.toResponseDTO(room);
    }

    @Override
    public List<RoomResponseDTO> getAllRooms() {
        logger.info("Retrieving all rooms");
        List<Room> rooms = roomRepository.findAll();
        if (rooms.isEmpty()) {
            logger.warn("No rooms found.");
            throw new RoomNotFoundException("No rooms found.....");
        }
        return roomMapper.toResponseDTO(rooms);
    }

    @Override
    public RoomResponseDTO updateRoomByRoomNumber(RoomRequestDTO roomRequestDTO, String roomNumber) {
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

        Room room=roomRepository.save(r1);
        
        logger.info("Room updated successfully: {}", room.getRoomNumber());
        return roomMapper.toResponseDTO(room);
    }
}
