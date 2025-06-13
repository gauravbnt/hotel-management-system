package com.example.hms.hotel_management_system.service;

import org.springframework.http.ResponseEntity;

import com.example.hms.hotel_management_system.dto.request.GuestRequestDTO;

public interface GuestService {
    public ResponseEntity<?> createGuest(GuestRequestDTO guest);
    public ResponseEntity<?> getAllGuest() ;
    public ResponseEntity<?> getGuestByEmail(String email);
    public ResponseEntity<?> updateGuestByEmail(GuestRequestDTO guest,String email);
    public ResponseEntity<?> deleteGuestByEmail(String email);
}
