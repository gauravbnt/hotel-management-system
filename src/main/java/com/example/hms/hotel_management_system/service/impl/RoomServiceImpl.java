package com.example.hms.hotel_management_system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hms.hotel_management_system.entity.Room;
import com.example.hms.hotel_management_system.repository.RoomRepository;
import com.example.hms.hotel_management_system.service.RoomService;

@Service
public class RoomServiceImpl implements RoomService{
    @Autowired
    RoomRepository roomRepository;

    public Room createRoom(Room room){
        return roomRepository.save(room);
    }
    public Room getRoomByRoomNumber(String roomNumber){
        return roomRepository.findRoomByRoomNumber(roomNumber);
    }

    public List<Room> getAvailableRooms(Boolean isAvailable){
        return roomRepository.findByIsAvailable(isAvailable);
    }
    public List<Room> getAllRooms(){
        return roomRepository.findAll();
    }

    public Room updateRoomByRoomNumber(Room room,String roomNumber){
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
