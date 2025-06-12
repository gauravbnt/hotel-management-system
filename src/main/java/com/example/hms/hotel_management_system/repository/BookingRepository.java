package com.example.hms.hotel_management_system.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hms.hotel_management_system.entity.Booking;
import com.example.hms.hotel_management_system.entity.Room;

@Repository
public interface BookingRepository extends JpaRepository<Booking ,UUID> {
    List<Booking> findByRoom(Room room);
    Booking findByRoom_RoomNumberAndGuest_Email(String roomNumber, String email);
    Boolean existsByRoom_RoomNumberAndGuest_Email(String roomNumber, String email);
}
