package com.example.hms.hotel_management_system.dto;

import java.sql.Timestamp;
import java.util.UUID;
import lombok.Data;

@Data
public class GuestResponseDTO {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private Timestamp createdAt;
}
