package com.example.hms.hotel_management_system.service;

import org.springframework.http.ResponseEntity;

import com.example.hms.hotel_management_system.dto.request.PaymentRequestDTO;

public interface PaymentService {
    public ResponseEntity<?> createPayment(PaymentRequestDTO paymentDTO);
    public ResponseEntity<?> getPaymentByTransactionId(String transactionId);
    public ResponseEntity<?> updatePaymentByTransactionId(PaymentRequestDTO payment,String transactionId);
}
