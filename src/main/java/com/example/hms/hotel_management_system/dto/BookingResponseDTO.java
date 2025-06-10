package com.example.hms.hotel_management_system.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import com.example.hms.hotel_management_system.enums.BookingStatus;
import com.example.hms.hotel_management_system.enums.RoomType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {
    private UUID id;
    private Timestamp checkInDate;
    private Timestamp checkOutDate;
    private BookingStatus bookingStatus;
    private BigDecimal totalAmount;
    private String email;
    private String roomNumber;
    private Boolean isAvailable;
    private Timestamp createdAt; 
    private RoomType roomType;   
    
    private PaymentResponseDTO payment;

}
