package com.example.hotelmanagementsystem.exception;

public class InvalidRoomRentException extends RuntimeException{
    public InvalidRoomRentException(String msg){
        super(msg);
    }
}
