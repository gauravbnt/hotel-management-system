package com.example.hms.hotel_management_system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hms.hotel_management_system.DTO.BookingDTO;
import com.example.hms.hotel_management_system.entity.Booking;
import com.example.hms.hotel_management_system.entity.Guest;
import com.example.hms.hotel_management_system.entity.Room;
import com.example.hms.hotel_management_system.repository.BookingRepository;
import com.example.hms.hotel_management_system.repository.GuestRepository;
import com.example.hms.hotel_management_system.repository.RoomRepository;
import com.example.hms.hotel_management_system.service.BookingService;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    GuestRepository guestRepository;

    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Room room = roomRepository.findRoomByRoomNumber(bookingDTO.getRoomNumber());
        Guest guest = guestRepository.findByEmail(bookingDTO.getEmail());
        try{
            
            if (!room.getIsAvailable()) {

                throw new IllegalStateException("Room " + room.getRoomNumber() + " is already booked.");
            }
           
        }
        catch (IllegalStateException e) {
            System.err.println("Error creating booking: " + e.getMessage());
            return null;

        }   
        Booking booking = new Booking();
        booking.setCheckInDate(bookingDTO.getCheckInDate());
        booking.setCheckOutDate(bookingDTO.getCheckOutDate());
        booking.setBookingStatus(bookingDTO.getBookingStatus());
        booking.setTotalAmount(bookingDTO.getTotalAmount());
        booking.setRoom(room);
        booking.setGuest(guest);

        room.setIsAvailable(false);
            
        Booking savedBooking = bookingRepository.save(booking);
        BookingDTO responseDTO = new BookingDTO();
        
        responseDTO.setId(savedBooking.getId());
        responseDTO.setCheckInDate(savedBooking.getCheckInDate());
        responseDTO.setCheckOutDate(savedBooking.getCheckOutDate());
        responseDTO.setBookingStatus(savedBooking.getBookingStatus());
        responseDTO.setTotalAmount(savedBooking.getTotalAmount());
        responseDTO.setEmail(savedBooking.getGuest().getEmail());
        responseDTO.setRoomNumber(savedBooking.getRoom().getRoomNumber());

        return responseDTO; 
    }

}
