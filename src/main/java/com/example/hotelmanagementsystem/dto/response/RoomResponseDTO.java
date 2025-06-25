package com.example.hotelmanagementsystem.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.hotelmanagementsystem.enums.RoomType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponseDTO {

    private UUID id;
    private String roomNumber;
    private RoomType roomType;
    private BigDecimal pricePerNight;
    private Boolean isAvailable;
    private Integer floorNumber;
    private String description;
}
