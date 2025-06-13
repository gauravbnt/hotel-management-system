package com.example.hms.hotel_management_system.service;



import org.springframework.http.ResponseEntity;

import com.example.hms.hotel_management_system.dto.request.BookingRequestDTO;

public interface BookingService {
    public ResponseEntity<?> createBooking(BookingRequestDTO bookingDTO);
    public ResponseEntity<?> getAllBookings();
    public ResponseEntity<?> updateBookingByRoomNumberAndEmail(String roomNumber, String email, BookingRequestDTO bookingRequestDTO);
}
