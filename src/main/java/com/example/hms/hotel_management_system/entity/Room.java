package com.example.hms.hotel_management_system.entity;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.hms.hotel_management_system.enums.RoomType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    private BigDecimal pricePerNight;
    private Boolean isAvailable;
    private Integer floorNumber;
    private String description;

}
