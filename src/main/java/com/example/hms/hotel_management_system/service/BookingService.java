package com.example.hms.hotel_management_system.service;


import java.util.List;

import com.example.hms.hotel_management_system.dto.BookingDTO;

public interface BookingService {
    public BookingDTO createBooking(BookingDTO bookingDTO); 
    public List<BookingDTO> getAllBookings();
    public BookingDTO updateBookingByRoomNumberAndEmail(String roomNumber,String email,BookingDTO bookingDTO);
}
