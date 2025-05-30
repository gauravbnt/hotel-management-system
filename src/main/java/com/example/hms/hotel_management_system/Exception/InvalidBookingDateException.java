package com.example.hms.hotel_management_system.exception;

public class InvalidBookingDateException extends RuntimeException{
    public InvalidBookingDateException(String msg){
        super(msg);
    }
}
