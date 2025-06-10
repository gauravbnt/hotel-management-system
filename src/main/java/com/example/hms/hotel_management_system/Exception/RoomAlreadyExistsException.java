package com.example.hms.hotel_management_system.exception;

public class RoomAlreadyExistsException extends RuntimeException{

    public RoomAlreadyExistsException(String msg)
    {
        super(msg);
    }

}
