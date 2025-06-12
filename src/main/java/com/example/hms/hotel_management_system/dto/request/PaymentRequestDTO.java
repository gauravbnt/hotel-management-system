package com.example.hms.hotel_management_system.dto.request;

import java.math.BigDecimal;

import com.example.hms.hotel_management_system.enums.PaymentMethod;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    private BigDecimal amountPaid;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotBlank(message = "Transaction ID is required")
   private String transactionId;

    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
}
