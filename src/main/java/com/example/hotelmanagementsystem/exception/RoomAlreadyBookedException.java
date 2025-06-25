package com.example.hotelmanagementsystem.exception;

public class RoomAlreadyBookedException extends RuntimeException {
    public RoomAlreadyBookedException(String msg)
    {
        super(msg);
    }
}
