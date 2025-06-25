package com.example.hotelmanagementsystem.exception;

public class GuestAlreadyExistsException extends RuntimeException {
    public GuestAlreadyExistsException(String message) {
        super(message);
    }
}

