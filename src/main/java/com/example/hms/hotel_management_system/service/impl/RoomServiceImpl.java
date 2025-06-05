package com.example.hms.hotel_management_system.service.Impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.hms.hotel_management_system.entity.Room;
import com.example.hms.hotel_management_system.exception.RoomAlreadyExistsException;
import com.example.hms.hotel_management_system.exception.RoomNotFoundException;
import com.example.hms.hotel_management_system.repository.RoomRepository;
import com.example.hms.hotel_management_system.service.RoomService;

@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    private final RoomRepository roomRepository;

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room createRoom(Room room) {
        logger.info("Creating room with room number: {}", room.getRoomNumber());
        if (roomRepository.findRoomByRoomNumber(room.getRoomNumber()) != null) {
            logger.warn("Room already exists with number: {}", room.getRoomNumber());
            throw new RoomAlreadyExistsException("Room already exists with number: " + room.getRoomNumber());
        }
        Room savedRoom = roomRepository.save(room);
        logger.info("Room created successfully: {}", savedRoom.getRoomNumber());
        return savedRoom;
    }

    @Override
    public Room getRoomByRoomNumber(String roomNumber) {
        logger.info("Retrieving room with room number: {}", roomNumber);
        Room room = roomRepository.findRoomByRoomNumber(roomNumber);
        if (room == null) {
            logger.error("Room not found with number: {}", roomNumber);
            throw new RoomNotFoundException("Room not found with number: " + roomNumber);
        }
        return room;
    }

    @Override
    public List<Room> getAvailableRooms(Boolean isAvailable) {
        logger.info("Retrieving available rooms with availability: {}", isAvailable);
        if (isAvailable == null) {
            logger.error("Availability flag is null.");
            throw new IllegalArgumentException("Availability flag must not be null.");
        }
        return roomRepository.findByIsAvailable(isAvailable);
    }

    @Override
    public List<Room> getAllRooms() {
        logger.info("Retrieving all rooms");
        List<Room> rooms = roomRepository.findAll();
        if (rooms.isEmpty()) {
            logger.warn("No rooms found.");
            throw new RoomNotFoundException("No rooms found.....");
        }
        return rooms;
    }

    @Override
    public Room updateRoomByRoomNumber(Room room, String roomNumber) {
        logger.info("Updating room with room number: {}", roomNumber);
        if (room == null) {
            logger.error("Room update data is null for room number: {}", roomNumber);
            throw new RoomNotFoundException("Room not found with number: " + roomNumber);
        }

        Room r1 = getRoomByRoomNumber(roomNumber);
        r1.setRoomNumber(room.getRoomNumber());
        r1.setRoomType(room.getRoomType());
        r1.setFloorNumber(room.getFloorNumber());
        r1.setDescription(room.getDescription());
        r1.setIsAvailable(room.getIsAvailable());
        r1.setPricePerNight(room.getPricePerNight());

        Room updatedRoom = roomRepository.save(r1);
        logger.info("Room updated successfully: {}", updatedRoom.getRoomNumber());
        return updatedRoom;
    }
}
