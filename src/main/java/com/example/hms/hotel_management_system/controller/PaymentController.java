package com.example.hms.hotel_management_system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hms.hotel_management_system.dto.PaymentDTO;
import com.example.hms.hotel_management_system.entity.Payment;
import com.example.hms.hotel_management_system.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService){
        this.paymentService=paymentService;
    }
    @PostMapping("/add-payment")
    public Payment createPayment(@RequestBody PaymentDTO payment)
    {
        return paymentService.createPayment(payment);
    }

    @GetMapping("/get-payment-by-trans-id")
    public Payment getPaymentByTransactionId(@PathVariable String transactionId){
        return paymentService.getPaymentByTransactionId(transactionId);
    }

    @PutMapping("update-payment")
    public Payment updatePaymentByTransactionId(@RequestBody Payment payment,@PathVariable String transactionId){
        return paymentService.updatePaymentByTransactionId(payment, transactionId);
    }
}
