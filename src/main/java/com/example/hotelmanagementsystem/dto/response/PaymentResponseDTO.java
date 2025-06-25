package com.example.hotelmanagementsystem.dto.response;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

import com.example.hotelmanagementsystem.enums.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private UUID id;
    private Timestamp paymentDate;
    private BigDecimal amountPaid;
    private PaymentMethod paymentMethod;
    private String transactionId;
    private String roomNumber;
    private String email;
}
