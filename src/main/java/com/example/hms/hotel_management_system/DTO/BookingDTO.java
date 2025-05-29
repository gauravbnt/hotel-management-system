package com.example.hms.hotel_management_system.DTO;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.UUID;

import com.example.hms.hotel_management_system.enums.BookingStatus;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    private UUID id;
    private Date checkInDate;
    private Date checkOutDate;
    private BookingStatus bookingStatus;
    private BigDecimal totalAmount;
    private String email;
    private String roomNumber;
    private Boolean isAvailable;
    private Timestamp createdAt; 

}
