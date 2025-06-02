package com.example.hms.hotel_management_system.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.example.hms.hotel_management_system.enums.RoomType;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(unique = true)
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    private BigDecimal pricePerNight;
    private Boolean isAvailable=true;
    private Integer floorNumber;
    private String description;

    @OneToMany(mappedBy ="room", cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    @JsonManagedReference("room-booking")
    List<Booking> booking;

}
