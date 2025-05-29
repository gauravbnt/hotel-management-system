package com.example.hms.hotel_management_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hms.hotel_management_system.DTO.BookingDTO;
import com.example.hms.hotel_management_system.Exception.RoomAlreadyBookedException;
import com.example.hms.hotel_management_system.service.BookingService;


@RestController
@RequestMapping("/booking")

public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping("/addBooking")
    public BookingDTO addBooking(@RequestBody BookingDTO bookingDTO){
        try{
           return bookingService.createBooking(bookingDTO);
        }
        catch(RoomAlreadyBookedException e)
        {
            System.out.println("Rooms are not available");
            return null;
        }
    }

    @GetMapping("/getAll")
    public List<BookingDTO> getAllBookings()
    {
        return bookingService.getAllBookings();
    }

    @PutMapping("/updateBooking")
    public BookingDTO updateBooking(@RequestParam String roomNumber,@RequestParam String email,@RequestBody BookingDTO bookingDTO) {
        return bookingService.updateBookingByRoomNumberAndEmail(roomNumber, email, bookingDTO);
    }
}
