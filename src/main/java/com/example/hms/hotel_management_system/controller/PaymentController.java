package com.example.hms.hotel_management_system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.hms.hotel_management_system.dto.PaymentRequestDTO;
import com.example.hms.hotel_management_system.service.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // add payment
    @PostMapping("/add-payment")
    public ResponseEntity<?> createPayment(@Valid @RequestBody PaymentRequestDTO payment) {
    
            logger.info("Creating payment for room: {} and email: {}", payment.getRoomNumber(), payment.getEmail());
            return paymentService.createPayment(payment);
        }

    // get payment by transaction id
    @GetMapping("/get-payment-by-trans-id/{transactionId}")
    public ResponseEntity<?> getPaymentByTransactionId(@PathVariable String transactionId) {

            logger.info("Fetching payment with transaction ID: {}", transactionId);
            return paymentService.getPaymentByTransactionId(transactionId);
        }

    // update payment
    @PutMapping("/update-payment/{transactionId}")
    public ResponseEntity<?> updatePaymentByTransactionId(@Valid @RequestBody PaymentRequestDTO payment,
            @PathVariable String transactionId) {
        
            logger.info("Updating payment with transaction ID: {}", transactionId);
            return paymentService.updatePaymentByTransactionId(payment, transactionId);    
    }
}
