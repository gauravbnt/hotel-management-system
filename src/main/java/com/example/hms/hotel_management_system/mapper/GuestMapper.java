package com.example.hms.hotel_management_system.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.hms.hotel_management_system.dto.GuestRequestDTO;
import com.example.hms.hotel_management_system.dto.GuestResponseDTO;
import com.example.hms.hotel_management_system.entity.Guest;

@Mapper(componentModel = "spring")
public interface GuestMapper {
    GuestResponseDTO toResponseDTO(Guest guest);
    List<GuestResponseDTO> toResponseDTO(List<Guest> guests);
    Guest toEntity(GuestRequestDTO guestRequestDTO);

}
