package com.example.hms.hotel_management_system.exception;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String msg){
        super(msg);
    }
}
