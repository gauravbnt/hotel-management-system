package com.example.hotelmanagementsystem.service;


import java.util.List;

import com.example.hotelmanagementsystem.dto.request.BookingRequestDTO;
import com.example.hotelmanagementsystem.dto.response.BookingResponseDTO;

public interface BookingService {
    public BookingResponseDTO createBooking(BookingRequestDTO bookingDTO); 
    public List<BookingResponseDTO> getAllBookings();
    public BookingResponseDTO updateBookingByRoomNumberAndEmail(String roomNumber,String email,BookingRequestDTO bookingDTO);
}
