package com.example.hms.hotel_management_system.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.hms.hotel_management_system.entity.Room;
import com.example.hms.hotel_management_system.exception.RoomAlreadyExistsException;
import com.example.hms.hotel_management_system.exception.RoomNotFoundException;
import com.example.hms.hotel_management_system.response.ApiResponse;
import com.example.hms.hotel_management_system.service.RoomService;

@RestController
@RequestMapping("/room")
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // add room
    @PostMapping("/add-room")
    public ResponseEntity<ApiResponse<Room>> createRoom(@RequestBody Room room) {
        try {
            logger.info("Creating room with number: {}", room.getRoomNumber());
            Room createdRoom = roomService.createRoom(room);

            ApiResponse<Room> response = new ApiResponse<>(
                    "Room created successfully",
                    HttpStatus.CREATED.value(),
                    createdRoom);

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RoomAlreadyExistsException e) {
            logger.warn("Room already exists: {}", room.getRoomNumber());
            ApiResponse<Room> response = new ApiResponse<>(
                    "Room already exists",
                    HttpStatus.CONFLICT.value(),
                    null);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } catch (Exception e) {
            logger.error("Internal server error while creating room", e);
            ApiResponse<Room> response = new ApiResponse<>(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get room by room number
    @GetMapping("/get-by-room-number/{roomNumber}")
    public ResponseEntity<ApiResponse<Room>> getRoomByRoomNumber(@PathVariable String roomNumber) {
        try {
            logger.info("Fetching room with number: {}", roomNumber);
            Room room = roomService.getRoomByRoomNumber(roomNumber);

            ApiResponse<Room> response = new ApiResponse<>(
                    "Room fetched successfully",
                    HttpStatus.OK.value(),
                    room);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RoomNotFoundException e) {
            logger.warn("Room not found: {}", roomNumber);
            ApiResponse<Room> response = new ApiResponse<>(
                    "Room not found",
                    HttpStatus.NOT_FOUND.value(),
                    null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Internal server error while fetching room", e);
            ApiResponse<Room> response = new ApiResponse<>(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get room by availability
    @GetMapping("/get-by-available")
    public ResponseEntity<ApiResponse<List<Room>>> getAvailableRooms(@RequestParam Boolean isAvailable) {
        try {
            logger.info("Fetching rooms with availability: {}", isAvailable);
            List<Room> rooms = roomService.getAvailableRooms(isAvailable);

            ApiResponse<List<Room>> response = new ApiResponse<>(
                    "Rooms fetched successfully",
                    HttpStatus.OK.value(),
                    rooms);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RoomNotFoundException e) {
            logger.warn("No rooms found for availability: {}", isAvailable);
            ApiResponse<List<Room>> response = new ApiResponse<>(
                    "No rooms found for given availability",
                    HttpStatus.NOT_FOUND.value(),
                    null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Internal server error while fetching available rooms", e);
            ApiResponse<List<Room>> response = new ApiResponse<>(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get all rooms
    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<Room>>> getAll() {
        try {
            logger.info("Fetching all rooms");
            List<Room> rooms = roomService.getAllRooms();

            ApiResponse<List<Room>> response = new ApiResponse<>(
                    "All rooms fetched successfully",
                    HttpStatus.OK.value(),
                    rooms);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RoomNotFoundException e) {
            logger.warn("No rooms found");
            ApiResponse<List<Room>> response = new ApiResponse<>(
                    "No rooms found",
                    HttpStatus.NOT_FOUND.value(),
                    null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Internal server error while fetching all rooms", e);
            ApiResponse<List<Room>> response = new ApiResponse<>(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // update room by room number
    @PutMapping("/update-room-by-room-number/{roomNumber}")
    public ResponseEntity<ApiResponse<Room>> updateRoomByRoomNumber(@RequestBody Room room,
            @PathVariable String roomNumber) {
        try {
            logger.info("Updating room with number: {}", roomNumber);
            Room updatedRoom = roomService.updateRoomByRoomNumber(room, roomNumber);

            ApiResponse<Room> response = new ApiResponse<>(
                    "Room updated successfully",
                    HttpStatus.OK.value(),
                    updatedRoom);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RoomNotFoundException e) {
            logger.warn("Room not found for update: {}", roomNumber);
            ApiResponse<Room> response = new ApiResponse<>(
                    "Room not found",
                    HttpStatus.NOT_FOUND.value(),
                    null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Internal server error while updating room", e);
            ApiResponse<Room> response = new ApiResponse<>(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
