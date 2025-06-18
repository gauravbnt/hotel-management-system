package com.example.hms.hotel_management_system.dto.request;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.example.hms.hotel_management_system.enums.BookingStatus;
import com.example.hms.hotel_management_system.enums.RoomType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestDTO {

    @NotNull(message = "Check-in date is required")
    private Timestamp checkInDate;

    @NotNull(message = "Check-out date is required")
    private Timestamp checkOutDate;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @NotNull(message = "Room type is required")
    private RoomType roomType;

    private BookingStatus bookingStatus;
    private BigDecimal totalAmount;

    @Valid
    private PaymentRequestDTO payment;
}
