package com.example.hms.hotel_management_system.service;

import java.util.List;

import com.example.hms.hotel_management_system.entity.Room;

public interface RoomService {
    public Room createRoom(Room room);
    public Room getRoomByRoomNumber(String roomNumber);
    public List<Room> getAvailableRooms(Boolean isAvailable);
    public Room updateRoomByRoomNumber(Room room,String roomNumber);

}
