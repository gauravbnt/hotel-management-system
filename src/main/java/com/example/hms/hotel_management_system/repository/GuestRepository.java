package com.example.hms.hotel_management_system.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hms.hotel_management_system.entity.Guest;

@Repository
public interface GuestRepository extends JpaRepository<Guest,UUID>{
    Guest findByEmailAndPhoneNumber(String email,String phoneNumber);
    void deleteByEmail(String email);
}
