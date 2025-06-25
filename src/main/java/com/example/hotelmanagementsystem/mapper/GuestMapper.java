package com.example.hotelmanagementsystem.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.hotelmanagementsystem.dto.request.GuestRequestDTO;
import com.example.hotelmanagementsystem.dto.response.GuestResponseDTO;
import com.example.hotelmanagementsystem.entity.Guest;

@Mapper(componentModel = "spring")
public interface GuestMapper {
    GuestResponseDTO toResponseDTO(Guest guest);
    List<GuestResponseDTO> toResponseDTOList(List<Guest> guests);
    Guest toEntity(GuestRequestDTO guestRequestDTO);

}
