package com.example.hms.hotel_management_system.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.hms.hotel_management_system.entity.Guest;
import com.example.hms.hotel_management_system.exception.GuestAlreadyExistsException;
import com.example.hms.hotel_management_system.exception.GuestNotFoundException;
import com.example.hms.hotel_management_system.service.GuestService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@RestController
@RequestMapping("/guest")
public class GuestController {

    private final GuestService guestService;

    @PostMapping("/add_guest")
    public Guest addGuest(@RequestBody Guest guest){
        try {
            return guestService.createGuest(guest);   
        }
        catch (GuestAlreadyExistsException e) {
            System.out.println("Guest Already Exists....!");
            return null;
        }
        catch(Exception e){
            System.out.println("Internal Server Error.....");
            return null;
        }
    }
    
    @GetMapping("/get_all")
    public List<Guest> getAllGuest()
    {
        try{
            return guestService.getAllGuest();
        }
        catch(GuestNotFoundException e){
            System.out.println("Guests Not found");
            return null;
        }
        catch(Exception e){
            System.out.println("Internal Server Error.....");
            return null;
        }
    }

    @GetMapping("/get_by_email/{email}")
    public Guest getGuestById(@PathVariable String email){
        try{
            return guestService.getGuestByEmail(email);
        }
        catch(GuestNotFoundException e){
            System.out.println("Guests Not found with email "+email);
            return null;
        }
        catch(Exception e){
            System.out.println("Internal Server Error.....");
            return null;
        }    
    }

    @PutMapping("/update_by_email/{email}")
    public Guest updateGuestById(@RequestBody Guest guest,@PathVariable String email){
        try{
            return guestService.updateGuestByEmail(guest, email);
        }
        catch(GuestNotFoundException e){
            System.out.println("Guests Not found with email "+email);
            return null;
        }
        catch(Exception e){
            System.out.println("Internal Server Error..");
            return null;
        }
        
    }

    @DeleteMapping("/delete_by_email/{email}")
    public String deleteGuestByEmail(@PathVariable String email)
    {
        try{
            guestService.deleteGuestByEmail(email);
        return "Guest deleted successfully...!";
        }
        catch(GuestNotFoundException e){
            System.out.println("Guests Not found with email "+email);
            return null;
        }
        catch(Exception e){
            System.out.println("Internal Server Error..");
            return null;
        }
        
    }
}
