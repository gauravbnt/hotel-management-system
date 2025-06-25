package com.example.hotelmanagementsystem.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hotelmanagementsystem.entity.Guest;

@Repository
public interface GuestRepository extends JpaRepository<Guest,UUID>{
    Guest findByEmail(String email);
    Boolean existsByEmail(String email);
    void deleteByEmail(String email);
}
