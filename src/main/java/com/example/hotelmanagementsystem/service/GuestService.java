package com.example.hotelmanagementsystem.service;

import java.util.List;

import com.example.hotelmanagementsystem.dto.request.GuestRequestDTO;
import com.example.hotelmanagementsystem.dto.response.GuestResponseDTO;

public interface GuestService {
    public GuestResponseDTO createGuest(GuestRequestDTO guest);
    public List<GuestResponseDTO> getAllGuest();
    public GuestResponseDTO getGuestByEmail(String email);
    public GuestResponseDTO updateGuestByEmail(GuestRequestDTO guest,String email);
    public void deleteGuestByEmail(String email);
}
