package com.example.hms.hotel_management_system.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hms.hotel_management_system.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment,UUID> {
    Payment findByTransactionId(String transactionId);
}
