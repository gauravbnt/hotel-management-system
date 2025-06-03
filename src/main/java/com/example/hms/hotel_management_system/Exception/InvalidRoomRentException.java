package com.example.hms.hotel_management_system.exception;

public class InvalidRoomRentException extends RuntimeException{
    public InvalidRoomRentException(String msg){
        super(msg);
    }
}
