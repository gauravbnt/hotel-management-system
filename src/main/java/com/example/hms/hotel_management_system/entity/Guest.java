package com.example.hms.hotel_management_system.entity;

import java.security.Timestamp;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;
    
    private String phoneNumber;
    private String address;
    private Timestamp createdAt;
}