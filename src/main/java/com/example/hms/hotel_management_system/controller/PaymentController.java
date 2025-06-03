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
import com.example.hms.hotel_management_system.exception.BookingNotFoundException;
import com.example.hms.hotel_management_system.exception.PaymentAlreadyExistsException;
import com.example.hms.hotel_management_system.exception.PaymentNotFoundException;
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
        try{
        return paymentService.createPayment(payment);
        }
        catch(BookingNotFoundException e){
            System.out.println("Booking not found!!");
            return null;
        }
        catch(PaymentAlreadyExistsException e){
            System.out.println("Payment is already done!!");
            return null;
        }
        catch(Exception e){
            System.out.println("Internal Server Error");
            return null;
        }
    }

    @GetMapping("/get-payment-by-trans-id/{transactionId}")
    public Payment getPaymentByTransactionId(@PathVariable String transactionId){
        try{
            return paymentService.getPaymentByTransactionId(transactionId);
        }
        catch(PaymentNotFoundException e){
            System.out.println("Payment not found for "+transactionId);
            return null;
        }
        catch(Exception e)
        {
            System.out.println("Internal Server Error");
            return null;
        }
        
    }

    @PutMapping("update-payment/{transactionId}")
    public Payment updatePaymentByTransactionId(@RequestBody Payment payment,@PathVariable String transactionId){
        try{
            return paymentService.updatePaymentByTransactionId(payment, transactionId);
        }
        catch(PaymentNotFoundException e){
            System.out.println("Payment not found for "+transactionId);
            return null;
        }
        catch(Exception e)
        {
            System.out.println("Internal server error");
            return null;
        }
        
    }
}
