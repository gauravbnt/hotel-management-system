package com.example.hotelmanagementsystem.exception;

public class PaymentAlreadyExistsException extends RuntimeException{
    public PaymentAlreadyExistsException(String msg){
        super(msg);
    }
}
