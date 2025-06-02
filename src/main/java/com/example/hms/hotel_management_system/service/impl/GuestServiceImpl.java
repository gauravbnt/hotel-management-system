package com.example.hms.hotel_management_system.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hms.hotel_management_system.entity.Guest;
import com.example.hms.hotel_management_system.exception.GuestAlreadyExistsException;
import com.example.hms.hotel_management_system.exception.GuestNotFoundException;
import com.example.hms.hotel_management_system.repository.GuestRepository;
import com.example.hms.hotel_management_system.service.GuestService;


@Service
public class GuestServiceImpl implements GuestService {
    
    private final GuestRepository guestRepository;
    public GuestServiceImpl(GuestRepository guestRepository){
        this.guestRepository=guestRepository;
    }
    
    @Override
    public Guest createGuest(Guest guest){
        if (guestRepository.findByEmail(guest.getEmail()) != null) {
            throw new GuestAlreadyExistsException("Guest already exists with email: " + guest.getEmail());
        }
        return guestRepository.save(guest);
    }

    @Override
    public List<Guest> getAllGuest(){

        List<Guest>guests=guestRepository.findAll();
        if (guests.isEmpty()) {
            throw new GuestNotFoundException("No guests found.");
        }
        return guests;

    }

    @Override
    public Guest getGuestByEmail(String email){
        Guest guest = guestRepository.findByEmail(email);
        if (guest == null) {
            throw new GuestNotFoundException("Guest not found with email: " + email);
        }
        return guest;    
    }

    @Override
    public Guest updateGuestByEmail(Guest updateGuest,String email){
        Guest guest= getGuestByEmail(email);
        if (guest == null) {
            throw new GuestNotFoundException("Cannot update. Guest not found with email: " + email);
        }
        guest.setAddress(updateGuest.getAddress());
        guest.setEmail(updateGuest.getEmail());
        guest.setFirstName(updateGuest.getFirstName());
        guest.setLastName(updateGuest.getLastName());
        guest.setPhoneNumber(updateGuest.getPhoneNumber());

        return guestRepository.save(guest);
    }
    
    @Transactional
    @Override
    public void deleteGuestByEmail(String email){
        Guest guest = guestRepository.findByEmail(email);
        if (guest == null) {
            throw new GuestNotFoundException("Cannot delete. Guest not found with email: " + email);
        }
        guestRepository.delete(guest);
    }
}   
