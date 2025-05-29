package com.example.hms.hotel_management_system.Exception;

public class RoomAlreadyBookedException extends RuntimeException {
    public RoomAlreadyBookedException(String msg)
    {
        super(msg);
    }
}
