package com.example.hms.hotel_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hms.hotel_management_system.DTO.BookingDTO;
import com.example.hms.hotel_management_system.service.BookingService;


@RestController
@RequestMapping("/booking")

public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping("/addBooking")
    public BookingDTO addBooking(@RequestBody BookingDTO bookingDTO){
        return bookingService.createBooking(bookingDTO);
    }
}
