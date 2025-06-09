package com.example.hms.hotel_management_system.service;


import java.util.List;

import com.example.hms.hotel_management_system.dto.BookingRequestDTO;
import com.example.hms.hotel_management_system.dto.BookingResponseDTO;

public interface BookingService {
    public BookingResponseDTO createBooking(BookingRequestDTO bookingDTO); 
    public List<BookingResponseDTO> getAllBookings();
    public BookingResponseDTO updateBookingByRoomNumberAndEmail(String roomNumber,String email,BookingRequestDTO bookingDTO);
}
