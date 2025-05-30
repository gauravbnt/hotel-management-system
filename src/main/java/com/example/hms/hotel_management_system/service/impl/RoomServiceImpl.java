package com.example.hms.hotel_management_system.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.hms.hotel_management_system.entity.Room;
import com.example.hms.hotel_management_system.exception.RoomAlreadyExistsException;
import com.example.hms.hotel_management_system.exception.RoomNotFoundException;
import com.example.hms.hotel_management_system.repository.RoomRepository;
import com.example.hms.hotel_management_system.service.RoomService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoomServiceImpl implements RoomService{
    
    private final RoomRepository roomRepository;

    public Room createRoom(Room room){
        if (roomRepository.findRoomByRoomNumber(room.getRoomNumber()) != null) {
            throw new RoomAlreadyExistsException("Room already exists with number: " + room.getRoomNumber());
        }
        return roomRepository.save(room);

    }
    public Room getRoomByRoomNumber(String roomNumber){
        Room room = roomRepository.findRoomByRoomNumber(roomNumber);
        if (room == null) {
            throw new RoomNotFoundException("Room not found with number: " + roomNumber);
        }
        return room;
    }

    public List<Room> getAvailableRooms(Boolean isAvailable){
        if (isAvailable == null) {
            throw new IllegalArgumentException("Availability flag must not be null.");
        }
        return roomRepository.findByIsAvailable(isAvailable);
    }
    public List<Room> getAllRooms(){
        List<Room> rooms = roomRepository.findAll();
        if (rooms.isEmpty()) {
            throw new RoomNotFoundException("No rooms found.....");
        }
        return rooms;
    }

    public Room updateRoomByRoomNumber(Room room,String roomNumber){
        if (room == null) {
            throw new RoomNotFoundException("Room not found with number: " + roomNumber);
        }
        Room r1= getRoomByRoomNumber(roomNumber);
        r1.setRoomNumber(room.getRoomNumber());
        r1.setRoomType(room.getRoomType());
        r1.setFloorNumber(room.getFloorNumber());
        r1.setDescription(room.getDescription());
        r1.setIsAvailable(room.getIsAvailable());
        r1.setPricePerNight(room.getPricePerNight());
        
        return roomRepository.save(r1);
        
    }

}
