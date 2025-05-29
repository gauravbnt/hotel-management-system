package com.example.hms.hotel_management_system.service.impl;

import java.sql.Date;
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
import com.example.hms.hotel_management_system.enums.BookingStatus;
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
        List<Room> allRooms = roomRepository.findAll();
        Guest guest = guestRepository.findByEmail(bookingDTO.getEmail());

        Date checkIn = bookingDTO.getCheckInDate();
        Date checkOut = bookingDTO.getCheckOutDate();

        if (checkIn == null || checkOut == null || !checkIn.before(checkOut)) {
            throw new IllegalArgumentException("Invalid check-in/check-out dates.");
        }
        if (allRooms == null || allRooms.isEmpty()) {
            throw new RoomAlreadyBookedException("No rooms available.");
        }
        if (guest == null) {
            throw new IllegalStateException("Guest not found with email: " + bookingDTO.getEmail());
        }

        Room selectRoom = null;

        for (Room room : allRooms) {

            List<Booking> roomBookings = bookingRepository.findByRoom(room);

            boolean isConflict = false;
            for (Booking booking : roomBookings) {
                Date existingCheckIn = booking.getCheckInDate();
                Date existingCheckOut = booking.getCheckOutDate();

            if (booking.getBookingStatus() != BookingStatus.CANCELLED) {
                    boolean overlaps = checkIn.before(existingCheckOut) && checkOut.after(existingCheckIn);
                    if (overlaps) {
                        isConflict = true;
                        break;
                    }
                }
            }
            if (!isConflict) {
                selectRoom = room;
                break;
            }
        }

        if (selectRoom == null) {
            throw new RoomAlreadyBookedException("No available rooms for the selected time range.");
        }

        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking.setRoom(selectRoom);
        booking.setGuest(guest);

        selectRoom.setIsAvailable(false);
        roomRepository.save(selectRoom);

        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toDTO(savedBooking);
    }

    public List<BookingDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        List<BookingDTO> dtoList = new ArrayList<>();
        for (Booking booking : bookings) {
            dtoList.add(bookingMapper.toDTO(booking));
        }
        return dtoList;
    }

    public BookingDTO updateBookingByRoomNumberAndEmail(String roomNumber, String email, BookingDTO bookingDTO) {
        Booking booking = bookingRepository.findByRoom_RoomNumberAndGuest_Email(roomNumber, email);

        if (booking == null) {
        throw new IllegalArgumentException("No booking found for room number: " + roomNumber + " and email: " + email);
        }
        
        Date newCheckIn = bookingDTO.getCheckInDate();
        Date newCheckOut = bookingDTO.getCheckOutDate();
        BookingStatus newStatus = bookingDTO.getBookingStatus();

        if (newStatus == BookingStatus.CANCELLED) {
            booking.setBookingStatus(BookingStatus.CANCELLED);
            booking.getRoom().setIsAvailable(true);  
            roomRepository.save(booking.getRoom());  
            bookingRepository.save(booking);         
            return bookingMapper.toDTO(booking);
        }
        if (newCheckIn == null || newCheckOut == null || !newCheckIn.before(newCheckOut)) {
            throw new IllegalArgumentException("Invalid check-in/check-out dates.");
        }

        List<Booking> roomBookings = bookingRepository.findByRoom(booking.getRoom());
        for (Booking book : roomBookings) {
            if (!book.getId().equals(booking.getId())&& book.getBookingStatus() != BookingStatus.CANCELLED) {
                boolean overlaps = newCheckIn.before(book.getCheckOutDate()) && newCheckOut.after(book.getCheckInDate());
                if (overlaps) {
                    throw new RoomAlreadyBookedException("Room already booked for the selected dates.");
                }
            }
        }
        booking.setCheckInDate(newCheckIn);
        booking.setCheckOutDate(newCheckOut);
        booking.setBookingStatus(newStatus);
        booking.setTotalAmount(bookingDTO.getTotalAmount());

        Booking updatedBooking = bookingRepository.save(booking);
        return bookingMapper.toDTO(updatedBooking);

    }
}
