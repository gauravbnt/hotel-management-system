package com.example.hms.hotel_management_system.entity;

import java.sql.Date;
import java.util.UUID;

import com.example.hms.hotel_management_system.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Entity
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    private String phoneNumber;
    
    @Enumerated(EnumType.STRING)
    private Role role;

    private Date joinedDate;

}
