package com.example.hms.hotel_management_system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.example.hms.hotel_management_system.dto.PaymentDTO;
import com.example.hms.hotel_management_system.entity.Payment;
import com.example.hms.hotel_management_system.exception.BookingNotFoundException;
import com.example.hms.hotel_management_system.exception.PaymentAlreadyExistsException;
import com.example.hms.hotel_management_system.exception.PaymentNotFoundException;
import com.example.hms.hotel_management_system.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/add-payment")
    public Payment createPayment(@RequestBody PaymentDTO payment) {
        try {
            logger.info("Creating payment for room: {} and email: {}", payment.getRoomNumber(), payment.getEmail());
            return paymentService.createPayment(payment);
        } catch (BookingNotFoundException e) {
            logger.warn("Booking not found for room {} and email {}", payment.getRoomNumber(), payment.getEmail());
            return null;
        } catch (PaymentAlreadyExistsException e) {
            logger.warn("Payment already exists for room {} and email {}", payment.getRoomNumber(), payment.getEmail());
            return null;
        } catch (Exception e) {
            logger.error("Unexpected error while creating payment", e);
            return null;
        }
    }

    @GetMapping("/get-payment-by-trans-id/{transactionId}")
    public Payment getPaymentByTransactionId(@PathVariable String transactionId) {
        try {
            logger.info("Fetching payment with transaction ID: {}", transactionId);
            return paymentService.getPaymentByTransactionId(transactionId);
        } catch (PaymentNotFoundException e) {
            logger.warn("Payment not found for transaction ID: {}", transactionId);
            return null;
        } catch (Exception e) {
            logger.error("Unexpected error while fetching payment", e);
            return null;
        }
    }

    @PutMapping("/update-payment/{transactionId}")
    public Payment updatePaymentByTransactionId(@RequestBody Payment payment, @PathVariable String transactionId) {
        try {
            logger.info("Updating payment with transaction ID: {}", transactionId);
            return paymentService.updatePaymentByTransactionId(payment, transactionId);
        } catch (PaymentNotFoundException e) {
            logger.warn("Payment not found for transaction ID: {}", transactionId);
            return null;
        } catch (Exception e) {
            logger.error("Unexpected error while updating payment", e);
            return null;
        }
    }
}
