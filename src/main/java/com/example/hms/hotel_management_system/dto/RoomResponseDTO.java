package com.example.hms.hotel_management_system.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.hms.hotel_management_system.enums.RoomType;

import lombok.Data;

@Data
public class RoomResponseDTO {

    private UUID id;
    private String roomNumber;
    private RoomType roomType;
    private BigDecimal pricePerNight;
    private Boolean isAvailable;
    private Integer floorNumber;
    private String description;
}
