package com.example.hms.hotel_management_system.exception;

public class PaymentAlreadyExistsException extends RuntimeException{
    public PaymentAlreadyExistsException(String msg){
        super(msg);
    }
}
