package com.example.hms.hotel_management_system.exception;

public class RoomAlreadyBookedException extends RuntimeException {
    public RoomAlreadyBookedException(String msg)
    {
        super(msg);
    }
}
