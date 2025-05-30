package com.example.hms.hotel_management_system.exception;

public class GuestAlreadyExistsException extends RuntimeException {
    public GuestAlreadyExistsException(String message) {
        super(message);
    }
}

