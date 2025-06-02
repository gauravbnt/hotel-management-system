package com.example.hms.hotel_management_system.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hms.hotel_management_system.entity.Room;
import com.example.hms.hotel_management_system.exception.RoomAlreadyExistsException;
import com.example.hms.hotel_management_system.exception.RoomNotFoundException;
import com.example.hms.hotel_management_system.service.RoomService;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService){
        this.roomService=roomService;
    }

    @PostMapping("/add-room")
    public Room createRoom(@RequestBody Room room){
        try{
            return roomService.createRoom(room);
        }
        catch(RoomAlreadyExistsException e)
        {
            System.out.println("Room Already Exists....!");
            return null;
        }
        catch(Exception e){
            System.out.println("Internal Server Error..");
            return null;
        }
        
    }

    @GetMapping("/get-by-room-number/{roomNumber}")
    public Room getRoomByRoomNumber(@PathVariable String roomNumber){
        try{
            return roomService.getRoomByRoomNumber(roomNumber);
        }
        catch(RoomNotFoundException e)
        {
            System.out.println("Room not found with "+roomNumber);
            return null;
        }
        catch(Exception e){
            System.out.println("Internal Server Error..");
            return null;
        }
    }

    @GetMapping("/get-by-available")
    public List<Room> getAvailableRooms(@RequestParam Boolean isAvailable){
        try{
            return roomService.getAvailableRooms(isAvailable);
        }
        catch(RoomNotFoundException e){
            System.out.println("Rooms Not found");
            return null;
        }
        catch(Exception e){
            System.out.println("Internal Server Error..");
            return null;
        }
        
    }

    @GetMapping("/get-all")
    public List<Room> getAll(){
        try{
            return roomService.getAllRooms();
        }
        catch(RoomNotFoundException e){
            System.out.println("Rooms Not found");
            return null;
        }
        catch(Exception e){
            System.out.println("Internal Server Error..");
            return null;
        }

    }
        

    @PutMapping("update-room-by-room-number/{roomNumber}")
    public Room updateRoomByRoomNumber(@RequestBody Room room,@PathVariable String roomNumber){
        try {
        return roomService.updateRoomByRoomNumber(room,roomNumber);            
        }
        catch (RoomNotFoundException e) {
            System.out.println("Rooms Not found");
            return null;
        }
        catch(Exception e){
            System.out.println("Internal Server Error..");
            return null;
        }
    }

}
