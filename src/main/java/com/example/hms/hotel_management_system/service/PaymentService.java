package com.example.hms.hotel_management_system.service;

import com.example.hms.hotel_management_system.dto.PaymentDTO;
import com.example.hms.hotel_management_system.entity.Payment;

public interface PaymentService {
    public Payment createPayment(PaymentDTO paymentDTO);
    public Payment getPaymentByTransactionId(String transactionId);
    public Payment updatePaymentByTransactionId(Payment payment,String transactionId);
}
