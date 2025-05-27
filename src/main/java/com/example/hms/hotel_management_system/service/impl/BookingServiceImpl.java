package com.example.hms.hotel_management_system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Booking createBooking(Booking booking) {
        Room room = roomRepository.findRoomByRoomNumber(booking.getRoom().getRoomNumber());

        Guest guest = guestRepository.findByEmail(booking.getGuest().getEmail());

        if (!room.getIsAvailable()) {

            throw new IllegalStateException("Room " + room.getRoomNumber() + " is already booked.");
        }
        booking.setRoom(room);
        booking.setGuest(guest);
        room.setIsAvailable(false);
        return bookingRepository.save(booking);
    }

}
