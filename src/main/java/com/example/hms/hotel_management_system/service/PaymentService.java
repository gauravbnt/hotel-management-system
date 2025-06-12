package com.example.hms.hotel_management_system.service;

import com.example.hms.hotel_management_system.dto.request.PaymentRequestDTO;
import com.example.hms.hotel_management_system.dto.response.PaymentResponseDTO;

public interface PaymentService {
    public PaymentResponseDTO createPayment(PaymentRequestDTO paymentDTO);
    public PaymentResponseDTO getPaymentByTransactionId(String transactionId);
    public PaymentResponseDTO updatePaymentByTransactionId(PaymentRequestDTO payment,String transactionId);
}
