package com.example.hms.hotel_management_system.entity;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.sql.Date;
import java.util.UUID;

import com.example.hms.hotel_management_system.enums.BookingStatus;

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
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private Date checkInDate;
    private Date checkOutDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private BigDecimal totalAmount;
    private Timestamp createdAt;
}




