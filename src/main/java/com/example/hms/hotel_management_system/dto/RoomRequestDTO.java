package com.example.hms.hotel_management_system.dto;

import java.math.BigDecimal;

import com.example.hms.hotel_management_system.enums.RoomType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class RoomRequestDTO {

    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @NotNull(message = "Room type is required")
    private RoomType roomType;

    @NotNull(message = "Price per night is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price per night must be greater than 0")
    private BigDecimal pricePerNight;

    private Boolean isAvailable = true;

    @NotNull(message = "Floor number is required")
    private Integer floorNumber;

    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;
}
