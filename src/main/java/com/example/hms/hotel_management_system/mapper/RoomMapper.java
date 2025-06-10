package com.example.hms.hotel_management_system.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.hms.hotel_management_system.dto.RoomRequestDTO;
import com.example.hms.hotel_management_system.dto.RoomResponseDTO;
import com.example.hms.hotel_management_system.entity.Room;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    Room toEntity(RoomRequestDTO dto);

    RoomResponseDTO toResponseDTO(Room room);

    List<RoomResponseDTO> toResponseDTO(List<Room> rooms);
}
