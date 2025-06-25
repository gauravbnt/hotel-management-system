package com.example.hotelmanagementsystem.service;

import com.example.hotelmanagementsystem.dto.request.PaymentRequestDTO;
import com.example.hotelmanagementsystem.dto.response.PaymentResponseDTO;

public interface PaymentService {
    public PaymentResponseDTO createPayment(PaymentRequestDTO paymentDTO);
    public PaymentResponseDTO getPaymentByTransactionId(String transactionId);
    public PaymentResponseDTO updatePaymentByTransactionId(PaymentRequestDTO payment,String transactionId);
}
