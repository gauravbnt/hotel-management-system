package com.example.hms.hotel_management_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hms.hotel_management_system.entity.Room;
import com.example.hms.hotel_management_system.service.RoomService;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/room")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @PostMapping("/addRoom")
    public Room createRoom(@RequestBody Room room){
        return roomService.createRoom(room);
    }

    @GetMapping("/getByRoomNumber/{roomNumber}")
    public Room getRoomByRoomNumber(@PathVariable String roomNumber){
        return roomService.getRoomByRoomNumber(roomNumber);
    }

    @GetMapping("/getByAvailable")
    public List<Room> getAvailableRooms(@RequestParam Boolean isAvailable){
        return roomService.getAvailableRooms(isAvailable);
    }

    @PutMapping("updateRoomByRoomNumber/{roomNumber}")
    public Room updateRoomByRoomNumber(@RequestBody Room room,@PathVariable String roomNumber){
        return roomService.updateRoomByRoomNumber(room,roomNumber);
    }

}
