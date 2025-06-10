package com.example.hms.hotel_management_system.service;

import java.util.List;

import com.example.hms.hotel_management_system.dto.GuestRequestDTO;
import com.example.hms.hotel_management_system.dto.GuestResponseDTO;

public interface GuestService {
    public GuestResponseDTO createGuest(GuestRequestDTO guest);
    public List<GuestResponseDTO> getAllGuest();
    public GuestResponseDTO getGuestByEmail(String email);
    public GuestResponseDTO updateGuestByEmail(GuestRequestDTO guest,String email);
    public void deleteGuestByEmail(String email);
}
