package com.example.hms.hotel_management_system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hms.hotel_management_system.DTO.BookingDTO;
import com.example.hms.hotel_management_system.Exception.RoomAlreadyBookedException;
import com.example.hms.hotel_management_system.entity.Booking;
import com.example.hms.hotel_management_system.entity.Guest;
import com.example.hms.hotel_management_system.entity.Room;
import com.example.hms.hotel_management_system.mapper.BookingMapper;
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

    @Autowired
    BookingMapper bookingMapper;

    @Transactional
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Room room = roomRepository.findRoomByRoomNumber(bookingDTO.getRoomNumber());
        Guest guest = guestRepository.findByEmail(bookingDTO.getEmail());
        try{
            if (room == null) {
                throw new RoomAlreadyBookedException("Room not found with number: " + bookingDTO.getRoomNumber());
            }
            if (guest == null) {
                throw new RoomAlreadyBookedException("Guest not found with email: " + bookingDTO.getEmail());
            }
            if (!room.getIsAvailable()) {

                throw new RoomAlreadyBookedException("Room " + room.getRoomNumber() + " is already booked.");
            }
            
        }
        catch (IllegalStateException e) {
            System.err.println("Error creating booking: " + e.getMessage());
            return null;

        }   
        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking.setRoom(room);
        booking.setGuest(guest);

        room.setIsAvailable(false);
            
        Booking savedBooking = bookingRepository.save(booking);
        BookingDTO responseDTO = bookingMapper.toDTO(savedBooking);
        
        return responseDTO; 
    }
    public List<BookingDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingDTO> dtoList = new ArrayList<>();
        for (Booking booking : bookings) {
            dtoList.add(bookingMapper.toDTO(booking));
        }
        return dtoList;
    }
}
