package com.example.hms.hotel_management_system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.hms.hotel_management_system.dto.RoomRequestDTO;
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
    public ResponseEntity<?> createRoom(@Valid @RequestBody RoomRequestDTO room) {
            logger.info("Creating room with number: {}", room.getRoomNumber());
            return roomService.createRoom(room);
            }

    // get room by room number
    @GetMapping("/get-by-room-number/{roomNumber}")
    public ResponseEntity<?> getRoomByRoomNumber(@PathVariable String roomNumber) {
    
            logger.info("Fetching room with number: {}", roomNumber);   
            return roomService.getRoomByRoomNumber(roomNumber);
        }

    // get room by availability
    @GetMapping("/get-by-available")
    public ResponseEntity<?> getAvailableRooms(@RequestParam Boolean isAvailable) {
            logger.info("Fetching rooms with availability: {}", isAvailable);
            return roomService.getAvailableRooms(isAvailable);
        }

    // get all rooms
    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
            logger.info("Fetching all rooms");
            return roomService.getAllRooms();
        
    }

    // update room by room number
    @PutMapping("/update-room-by-room-number/{roomNumber}")
    public ResponseEntity<?> updateRoomByRoomNumber(@Valid @RequestBody RoomRequestDTO room,
            @PathVariable String roomNumber) {
            return roomService.updateRoomByRoomNumber(room, roomNumber);
        }
}
