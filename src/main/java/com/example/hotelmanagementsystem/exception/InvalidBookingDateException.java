package com.example.hotelmanagementsystem.exception;

public class InvalidBookingDateException extends RuntimeException{
    public InvalidBookingDateException(String msg){
        super(msg);
    }
}
