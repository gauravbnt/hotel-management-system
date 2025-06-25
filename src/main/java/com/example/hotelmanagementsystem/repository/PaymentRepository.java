package com.example.hotelmanagementsystem.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hotelmanagementsystem.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment,UUID> {
    Payment findByTransactionId(String transactionId);
}
