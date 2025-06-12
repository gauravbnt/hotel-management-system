package com.example.hms.hotel_management_system.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.hms.hotel_management_system.dto.request.RoomRequestDTO;
import com.example.hms.hotel_management_system.dto.response.RoomResponseDTO;
import com.example.hms.hotel_management_system.response.SuccessResponse;
import com.example.hms.hotel_management_system.service.RoomService;
import jakarta.validation.Valid;

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
    public ResponseEntity<SuccessResponse<RoomResponseDTO>> createRoom(@Valid @RequestBody RoomRequestDTO room) {
        logger.info("Creating room with number: {}", room.getRoomNumber());
        RoomResponseDTO createdRoom = roomService.createRoom(room);
        SuccessResponse<RoomResponseDTO> response = new SuccessResponse<>("Room created successfully",HttpStatus.CREATED.value(),createdRoom);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    // get room by room number
    @GetMapping("/get-by-room-number/{roomNumber}")
    public ResponseEntity<SuccessResponse<RoomResponseDTO>> getRoomByRoomNumber(@PathVariable String roomNumber) {
        logger.info("Fetching room with number: {}", roomNumber);
        RoomResponseDTO room = roomService.getRoomByRoomNumber(roomNumber);
        SuccessResponse<RoomResponseDTO> response = new SuccessResponse<>("Room fetched successfully",HttpStatus.OK.value(),room);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    // get room by availability
    @GetMapping("/get-by-available")
    public ResponseEntity<SuccessResponse<List<RoomResponseDTO>>> getAvailableRooms(@RequestParam Boolean isAvailable) {
        logger.info("Fetching rooms with availability: {}", isAvailable);
        List<RoomResponseDTO> rooms = roomService.getAvailableRooms(isAvailable);
        SuccessResponse<List<RoomResponseDTO>> response = new SuccessResponse<>("Rooms fetched successfully",HttpStatus.OK.value(),rooms);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    // get all rooms
    @GetMapping("/get-all")
    public ResponseEntity<SuccessResponse<List<RoomResponseDTO>>> getAll() {
        logger.info("Fetching all rooms");
        List<RoomResponseDTO> rooms = roomService.getAllRooms();
        SuccessResponse<List<RoomResponseDTO>> response = new SuccessResponse<>("All rooms fetched successfully",HttpStatus.OK.value(),rooms);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    // update room by room number
    @PutMapping("/update-room-by-room-number/{roomNumber}")
    public ResponseEntity<SuccessResponse<RoomResponseDTO>> updateRoomByRoomNumber(@Valid @RequestBody RoomRequestDTO room,@PathVariable String roomNumber) {
        logger.info("Updating room with number: {}", roomNumber);
        RoomResponseDTO updatedRoom = roomService.updateRoomByRoomNumber(room, roomNumber);
        SuccessResponse<RoomResponseDTO> response = new SuccessResponse<>("Room updated successfully",HttpStatus.OK.value(),updatedRoom);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
