package com.example.hms.hotel_management_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.hms.hotel_management_system.entity.Guest;
import com.example.hms.hotel_management_system.service.GuestService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/guest")
public class GuestController {

    @Autowired
    private GuestService guestService;

    @PostMapping("/addGuest")
    public Guest addGuest(@RequestBody Guest guest){
        return guestService.createGuest(guest);
    }
    
    @GetMapping("/getAll")
    public List<Guest> getAllGuest()
    {
        return guestService.getAllGuest();
    }

    @GetMapping("/getByEmail/{email}")//get_guest
    public Guest getGuestById(@PathVariable String email){
      return guestService.getGuestByEmail(email);
    
    }

    @PutMapping("/updateByEmail/{email}")
    public Guest updateGuestById(@RequestBody Guest guest,@PathVariable String email){
        return guestService.updateGuestByEmail(guest, email);
    }

    @DeleteMapping("/deleteByEmail/{email}")
    public String deleteGuestByEmail(@PathVariable String email)
    {
        guestService.deleteGuestByEmail(email);
        System.out.println("Delete operation....");
        return "Guest deleted successfully...!";
    }
}
