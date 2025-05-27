package com.example.hms.hotel_management_system.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.UUID;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;

import com.example.hms.hotel_management_system.enums.BookingStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private Date checkInDate;
    private Date checkOutDate;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    private BigDecimal totalAmount;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;


    @ManyToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="guest_id")
    @JsonBackReference
    private Guest guest;

    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="room_id")
    private Room room;

}

