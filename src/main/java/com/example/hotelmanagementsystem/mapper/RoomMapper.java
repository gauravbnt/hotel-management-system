package com.example.hotelmanagementsystem.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.hotelmanagementsystem.dto.request.RoomRequestDTO;
import com.example.hotelmanagementsystem.dto.response.RoomResponseDTO;
import com.example.hotelmanagementsystem.entity.Room;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    Room toEntity(RoomRequestDTO dto);

    RoomResponseDTO toResponseDTO(Room room);
    List<RoomResponseDTO> toResponseDTOList(List<Room> rooms);
}
