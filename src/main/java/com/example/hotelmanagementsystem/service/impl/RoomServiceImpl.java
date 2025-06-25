package com.example.hotelmanagementsystem.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.hotelmanagementsystem.exception.*;
import com.example.hotelmanagementsystem.dto.request.RoomRequestDTO;
import com.example.hotelmanagementsystem.dto.response.RoomResponseDTO;
import com.example.hotelmanagementsystem.entity.Room;
import com.example.hotelmanagementsystem.mapper.RoomMapper;
import com.example.hotelmanagementsystem.repository.RoomRepository;
import com.example.hotelmanagementsystem.service.RoomService;

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
    
        Room room=roomMapper.toEntity(roomRequestDTO);

        logger.info("Creating room with room number: {}", room.getRoomNumber());
        if (roomRepository.existsByRoomNumber(room.getRoomNumber())) {
            logger.warn("Room already exists with number: {}", room.getRoomNumber());
            throw new RoomAlreadyExistsException("Room already exists with number: " + room.getRoomNumber());
        }
        
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
        return roomMapper.toResponseDTOList(room);
    }

    @Override
    public List<RoomResponseDTO> getAllRooms() {
        logger.info("Retrieving all rooms");
        List<Room> rooms = roomRepository.findAll();
        if (rooms.isEmpty()) {
            logger.warn("No rooms found.");
            throw new RoomNotFoundException("No rooms found.....");
        }
        return roomMapper.toResponseDTOList(rooms);
    }

    @Override
    public RoomResponseDTO updateRoomByRoomNumber(RoomRequestDTO roomRequestDTO, String roomNumber) {
        Room r2=roomMapper.toEntity(roomRequestDTO);
        logger.info("Updating room with room number: {}", roomNumber);
        if (r2 == null) {
            logger.error("Room update data is null for room number: {}", roomNumber);
            throw new RoomNotFoundException("Room not found with number: " + roomNumber);
        }

        Room r1 = roomRepository.findRoomByRoomNumber(roomNumber); 
        r1.setRoomNumber(r2.getRoomNumber());
        r1.setRoomType(r2.getRoomType());
        r1.setFloorNumber(r2.getFloorNumber());
        r1.setDescription(r2.getDescription());
        r1.setIsAvailable(r2.getIsAvailable());
        r1.setPricePerNight(r2.getPricePerNight());

        Room room=roomRepository.save(r1);
        
        logger.info("Room updated successfully: {}", room.getRoomNumber());
        return roomMapper.toResponseDTO(room);
    }
}
