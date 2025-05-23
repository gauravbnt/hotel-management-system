package com.example.hms.hotel_management_system.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hms.hotel_management_system.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking ,UUID> {

}
