package com.example.hotelmanagementsystem.exception;

public class MethodArgumentNotValidException extends RuntimeException {
    public MethodArgumentNotValidException(String msg)
    {
        super(msg);
    }
}
