package com.example.hms.hotel_management_system.entity;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.UUID;

import com.example.hms.hotel_management_system.enums.PaymentMethod;

import jakarta.persistence.Entity;
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
//@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private BigDecimal amountPaid;
    private Timestamp paymentDate;


    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String transactionId;

}
