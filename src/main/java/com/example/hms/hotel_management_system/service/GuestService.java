package com.example.hms.hotel_management_system.service;

import java.util.List;

import com.example.hms.hotel_management_system.entity.Guest;

public interface GuestService {
    public Guest createGuest(Guest guest);
    public List<Guest> getAllGuest();
    public Guest getGuestByEmailAndPhoneNumber(String email,String phoneNumber);
    public Guest updateGuestByEmailAndPhoneNumber(Guest guest,String email,String phoneNumber);
    public void deleteGuestByEmail(String email);
}
