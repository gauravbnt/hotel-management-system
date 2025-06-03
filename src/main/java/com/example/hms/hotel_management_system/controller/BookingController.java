package com.example.hms.hotel_management_system.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hms.hotel_management_system.dto.BookingDTO;
import com.example.hms.hotel_management_system.exception.BookingNotFoundException;
import com.example.hms.hotel_management_system.exception.RoomAlreadyBookedException;
import com.example.hms.hotel_management_system.service.BookingService;



@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService){
        this.bookingService=bookingService;
    }

    @PostMapping("/add-booking")
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
            System.out.println("Something went wrong.."+e);
            return null;
        }
    }

    @GetMapping("/get-all")
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

    @PutMapping("/update-booking")
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
