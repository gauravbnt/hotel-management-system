package com.example.hms.hotel_management_system.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hms.hotel_management_system.DTO.BookingDTO;
import com.example.hms.hotel_management_system.exception.BookingNotFoundException;
import com.example.hms.hotel_management_system.exception.RoomAlreadyBookedException;
import com.example.hms.hotel_management_system.service.BookingService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/add_booking")
    public BookingDTO addBooking(@RequestBody BookingDTO bookingDTO){
        try{
           return bookingService.createBooking(bookingDTO);
        }
        catch(RoomAlreadyBookedException e)
        {
            System.out.println("Rooms are not available");
            return null;
        }
        catch (Exception e) {
            System.out.println("Something went wrong..");
            return null;
        }
    }

    @GetMapping("/get_all")
    public List<BookingDTO> getAllBookings()
    {
        try{
            return bookingService.getAllBookings();
        }
        catch(BookingNotFoundException e)
        {
            System.out.println("No Bookings found..");
            return null;
        }
        catch (Exception e) {
            System.out.println("Something went wrong..");
            return null;
        }
    }

    @PutMapping("/update_booking")
    public BookingDTO updateBooking(@RequestParam String roomNumber,@RequestParam String email,@RequestBody BookingDTO bookingDTO) {
        try{
            return bookingService.updateBookingByRoomNumberAndEmail(roomNumber, email, bookingDTO);
        }
        catch(BookingNotFoundException e)
        {
            System.out.println("No Booking found for "+email+" at "+roomNumber);
            return null;
        }
        catch (Exception e) {
            System.out.println("Something went wrong..");
            return null;
        }
        
        
    }
}
