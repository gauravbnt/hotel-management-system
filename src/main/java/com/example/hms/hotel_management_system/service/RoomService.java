package com.example.hms.hotel_management_system.service;

import java.util.List;

import com.example.hms.hotel_management_system.dto.request.RoomRequestDTO;
import com.example.hms.hotel_management_system.dto.response.RoomResponseDTO;

public interface RoomService {
    public RoomResponseDTO createRoom(RoomRequestDTO room);
    public RoomResponseDTO getRoomByRoomNumber(String roomNumber);
    public List<RoomResponseDTO> getAvailableRooms(Boolean isAvailable);
    public RoomResponseDTO updateRoomByRoomNumber(RoomRequestDTO room,String roomNumber);
    public List<RoomResponseDTO> getAllRooms();
}
