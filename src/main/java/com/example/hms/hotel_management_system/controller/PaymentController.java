package com.example.hms.hotel_management_system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.hms.hotel_management_system.dto.PaymentDTO;
import com.example.hms.hotel_management_system.entity.Payment;
import com.example.hms.hotel_management_system.exception.BookingNotFoundException;
import com.example.hms.hotel_management_system.exception.PaymentAlreadyExistsException;
import com.example.hms.hotel_management_system.exception.PaymentNotFoundException;
import com.example.hms.hotel_management_system.response.ApiResponse;
import com.example.hms.hotel_management_system.service.PaymentService;

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
    public ResponseEntity<ApiResponse<Payment>> createPayment(@RequestBody PaymentDTO payment) {
        try {
            logger.info("Creating payment for room: {} and email: {}", payment.getRoomNumber(), payment.getEmail());
            Payment savedPayment = paymentService.createPayment(payment);

            ApiResponse<Payment> response = new ApiResponse<>(
                    "Payment created successfully",
                    HttpStatus.CREATED.value(),
                    savedPayment);

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (BookingNotFoundException e) {
            logger.warn("Booking not found for room {} and email {}", payment.getRoomNumber(), payment.getEmail());

            ApiResponse<Payment> response = new ApiResponse<>(
                    "Booking not found for given room and email",
                    HttpStatus.NOT_FOUND.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (PaymentAlreadyExistsException e) {
            logger.warn("Payment already exists for room {} and email {}", payment.getRoomNumber(), payment.getEmail());

            ApiResponse<Payment> response = new ApiResponse<>(
                    "Payment already exists for given room and email",
                    HttpStatus.CONFLICT.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.CONFLICT);

        } catch (Exception e) {
            logger.error("Unexpected error while creating payment", e);

            ApiResponse<Payment> response = new ApiResponse<>(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get payment by transaction id
    @GetMapping("/get-payment-by-trans-id/{transactionId}")
    public ResponseEntity<ApiResponse<Payment>> getPaymentByTransactionId(@PathVariable String transactionId) {
        try {
            logger.info("Fetching payment with transaction ID: {}", transactionId);
            Payment payment = paymentService.getPaymentByTransactionId(transactionId);

            ApiResponse<Payment> response = new ApiResponse<>(
                    "Payment fetched successfully",
                    HttpStatus.OK.value(),
                    payment);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (PaymentNotFoundException e) {
            logger.warn("Payment not found for transaction ID: {}", transactionId);

            ApiResponse<Payment> response = new ApiResponse<>(
                    "Payment not found for given transaction ID",
                    HttpStatus.NOT_FOUND.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            logger.error("Unexpected error while fetching payment", e);

            ApiResponse<Payment> response = new ApiResponse<>(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // update payment
    @PutMapping("/update-payment/{transactionId}")
    public ResponseEntity<ApiResponse<Payment>> updatePaymentByTransactionId(@RequestBody Payment payment,
            @PathVariable String transactionId) {
        try {
            logger.info("Updating payment with transaction ID: {}", transactionId);
            Payment updatedPayment = paymentService.updatePaymentByTransactionId(payment, transactionId);

            ApiResponse<Payment> response = new ApiResponse<>(
                    "Payment updated successfully",
                    HttpStatus.OK.value(),
                    updatedPayment);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (PaymentNotFoundException e) {
            logger.warn("Payment not found for transaction ID: {}", transactionId);

            ApiResponse<Payment> response = new ApiResponse<>(
                    "Payment not found for given transaction ID",
                    HttpStatus.NOT_FOUND.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            logger.error("Unexpected error while updating payment", e);

            ApiResponse<Payment> response = new ApiResponse<>(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
