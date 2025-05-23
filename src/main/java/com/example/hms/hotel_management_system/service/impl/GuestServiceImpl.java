package com.example.hms.hotel_management_system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hms.hotel_management_system.entity.Guest;
import com.example.hms.hotel_management_system.repository.GuestRepository;
import com.example.hms.hotel_management_system.service.GuestService;

@Service
public class GuestServiceImpl implements GuestService {
    @Autowired
    GuestRepository guestRepository;

    public Guest createGuest(Guest guest){
        return guestRepository.save(guest);
    }

    public List<Guest> getAllGuest(){
        return guestRepository.findAll();
    }

    public Guest getGuestByEmailAndPhoneNumber(String email,String phoneNumber){
        return guestRepository.findByEmailAndPhoneNumber(email,phoneNumber);
    
    }

    public Guest updateGuestByEmailAndPhoneNumber(Guest updateGuest,String email,String phoneNumber){
        Guest guest= getGuestByEmailAndPhoneNumber(email,phoneNumber);
        guest.setAddress(updateGuest.getAddress());
        guest.setEmail(updateGuest.getEmail());
        guest.setFirstName(updateGuest.getFirstName());
        guest.setLastName(updateGuest.getLastName());
        guest.setPhoneNumber(updateGuest.getPhoneNumber());

        return guestRepository.save(guest);
    }
    
    @Transactional
    public void deleteGuestByEmail(String email){
        guestRepository.deleteByEmail(email);
    }
}   
