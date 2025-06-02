package com.example.hms.hotel_management_system.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import com.example.hms.hotel_management_system.enums.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private UUID id;
    private BigDecimal amountPaid;
    private Timestamp paymentDate;
    private PaymentMethod paymentMethod;
    private String transactionId;
    private String roomNumber;
    private String email;
}
