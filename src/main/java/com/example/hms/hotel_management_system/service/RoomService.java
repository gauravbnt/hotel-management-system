package com.example.hms.hotel_management_system.service;

import org.springframework.http.ResponseEntity;

import com.example.hms.hotel_management_system.dto.RoomRequestDTO;

public interface RoomService {
    public ResponseEntity<?> createRoom(RoomRequestDTO room);
    public ResponseEntity<?> getRoomByRoomNumber(String roomNumber);
    public ResponseEntity<?> getAvailableRooms(Boolean isAvailable);
    public ResponseEntity<?> updateRoomByRoomNumber(RoomRequestDTO room,String roomNumber);
    public ResponseEntity<?> getAllRooms();
}
