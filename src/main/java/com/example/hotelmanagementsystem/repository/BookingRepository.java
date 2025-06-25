package com.example.hotelmanagementsystem.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hotelmanagementsystem.entity.Booking;
import com.example.hotelmanagementsystem.entity.Room;

@Repository
public interface BookingRepository extends JpaRepository<Booking ,UUID> {
    List<Booking> findByRoom(Room room);
    Booking findByRoom_RoomNumberAndGuest_Email(String roomNumber, String email);
    Boolean existsByRoom_RoomNumberAndGuest_Email(String roomNumber, String email);
}
