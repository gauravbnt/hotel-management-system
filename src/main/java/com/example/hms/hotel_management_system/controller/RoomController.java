package com.example.hms.hotel_management_system.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.example.hms.hotel_management_system.entity.Room;
import com.example.hms.hotel_management_system.exception.RoomAlreadyExistsException;
import com.example.hms.hotel_management_system.exception.RoomNotFoundException;
import com.example.hms.hotel_management_system.service.RoomService;

@RestController
@RequestMapping("/room")
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/add-room")
    public Room createRoom(@RequestBody Room room) {
        try {
            logger.info("Creating room with number: {}", room.getRoomNumber());
            return roomService.createRoom(room);
        } catch (RoomAlreadyExistsException e) {
            logger.warn("Room already exists: {}", room.getRoomNumber());
            return null;
        } catch (Exception e) {
            logger.error("Internal server error while creating room", e);
            return null;
        }
    }

    @GetMapping("/get-by-room-number/{roomNumber}")
    public Room getRoomByRoomNumber(@PathVariable String roomNumber) {
        try {
            logger.info("Fetching room with number: {}", roomNumber);
            return roomService.getRoomByRoomNumber(roomNumber);
        } catch (RoomNotFoundException e) {
            logger.warn("Room not found: {}", roomNumber);
            return null;
        } catch (Exception e) {
            logger.error("Internal server error while fetching room", e);
            return null;
        }
    }

    @GetMapping("/get-by-available")
    public List<Room> getAvailableRooms(@RequestParam Boolean isAvailable) {
        try {
            logger.info("Fetching rooms with availability: {}", isAvailable);
            return roomService.getAvailableRooms(isAvailable);
        } catch (RoomNotFoundException e) {
            logger.warn("No rooms found for availability: {}", isAvailable);
            return null;
        } catch (Exception e) {
            logger.error("Internal server error while fetching available rooms", e);
            return null;
        }
    }

    @GetMapping("/get-all")
    public List<Room> getAll() {
        try {
            logger.info("Fetching all rooms");
            return roomService.getAllRooms();
        } catch (RoomNotFoundException e) {
            logger.warn("No rooms found");
            return null;
        } catch (Exception e) {
            logger.error("Internal server error while fetching all rooms", e);
            return null;
        }
    }

    @PutMapping("update-room-by-room-number/{roomNumber}")
    public Room updateRoomByRoomNumber(@RequestBody Room room, @PathVariable String roomNumber) {
        try {
            logger.info("Updating room with number: {}", roomNumber);
            return roomService.updateRoomByRoomNumber(room, roomNumber);
        } catch (RoomNotFoundException e) {
            logger.warn("Room not found for update: {}", roomNumber);
            return null;
        } catch (Exception e) {
            logger.error("Internal server error while updating room", e);
            return null;
        }
    }
}
