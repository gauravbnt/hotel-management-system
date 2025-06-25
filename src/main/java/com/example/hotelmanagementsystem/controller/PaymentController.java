package com.example.hotelmanagementsystem.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.hotelmanagementsystem.dto.request.PaymentRequestDTO;
import com.example.hotelmanagementsystem.dto.response.PaymentResponseDTO;
import com.example.hotelmanagementsystem.response.SuccessResponse;
import com.example.hotelmanagementsystem.service.PaymentService;

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
    public ResponseEntity<SuccessResponse<PaymentResponseDTO>> createPayment(
            @Valid @RequestBody PaymentRequestDTO payment) {
        logger.info("Creating payment for room: {} and email: {}", payment.getRoomNumber(), payment.getEmail());
        PaymentResponseDTO savedPayment = paymentService.createPayment(payment);

        SuccessResponse<PaymentResponseDTO> response = new SuccessResponse<>("Payment created successfully",HttpStatus.CREATED.value(), savedPayment);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // get payment by transaction id
    @GetMapping("/get-payment-by-trans-id/{transactionId}")
    public ResponseEntity<SuccessResponse<PaymentResponseDTO>> getPaymentByTransactionId(
            @PathVariable String transactionId) {
        logger.info("Fetching payment with transaction ID: {}", transactionId);
        PaymentResponseDTO payment = paymentService.getPaymentByTransactionId(transactionId);

        SuccessResponse<PaymentResponseDTO> response = new SuccessResponse<>("Payment fetched successfully",HttpStatus.OK.value(), payment);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // update payment
    @PutMapping("/update-payment/{transactionId}")
    public ResponseEntity<SuccessResponse<PaymentResponseDTO>> updatePaymentByTransactionId(
            @Valid @RequestBody PaymentRequestDTO payment, @PathVariable String transactionId) {
        logger.info("Updating payment with transaction ID: {}", transactionId);
        PaymentResponseDTO updatedPayment = paymentService.updatePaymentByTransactionId(payment, transactionId);

        SuccessResponse<PaymentResponseDTO> response = new SuccessResponse<>("Payment updated successfully",HttpStatus.OK.value(), updatedPayment);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
