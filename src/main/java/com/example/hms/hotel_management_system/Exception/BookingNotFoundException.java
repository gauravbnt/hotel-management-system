package com.example.hms.hotel_management_system.exception;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String msg)
    {
        super(msg);
    }
}
