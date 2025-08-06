package com.example.hotelmanagementsystem.controller.integration;

import com.example.hotelmanagementsystem.dto.request.BookingRequestDTO;
import com.example.hotelmanagementsystem.dto.request.GuestRequestDTO;
import com.example.hotelmanagementsystem.dto.request.PaymentRequestDTO;
import com.example.hotelmanagementsystem.dto.request.RoomRequestDTO;
import com.example.hotelmanagementsystem.enums.BookingStatus;
import com.example.hotelmanagementsystem.enums.PaymentMethod;
import com.example.hotelmanagementsystem.enums.RoomType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingRequestDTO bookingRequestDTO;

    private String roomNumber="101";
    private String email="gcp2306@gmail.com";

    @BeforeEach
    void setUp() throws Exception {
        // Add guest
        GuestRequestDTO guest = new GuestRequestDTO();
        guest.setEmail(email);
        guest.setFirstName("Test");
        guest.setLastName("User");
        guest.setPhoneNumber("1234567890");
        guest.setAddress("Pune");

        mockMvc.perform(post("/guest/add-guest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(guest)))
                .andExpect(status().isCreated());

        // Add room
        RoomRequestDTO room = new RoomRequestDTO(roomNumber, RoomType.SINGLE,
                BigDecimal.valueOf(1000), true, 1, "Ground floor");
        mockMvc.perform(post("/room/add-room")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isCreated());

        PaymentRequestDTO payment=new PaymentRequestDTO(BigDecimal.valueOf(2000),PaymentMethod.CASH,"TXN1234",roomNumber,email);

            bookingRequestDTO=new BookingRequestDTO();
            bookingRequestDTO.setEmail(email);
            bookingRequestDTO.setRoomNumber(roomNumber);
            bookingRequestDTO.setCheckInDate(Timestamp.valueOf(LocalDateTime.of(2025, 6, 21, 12, 0)));
            bookingRequestDTO.setCheckOutDate(Timestamp.valueOf(LocalDateTime.of(2025, 6, 23, 12, 0)));
            bookingRequestDTO.setRoomType(RoomType.SINGLE);
            bookingRequestDTO.setBookingStatus(BookingStatus.BOOKED);
            bookingRequestDTO.setTotalAmount(BigDecimal.valueOf(2000));
            bookingRequestDTO.setPayment(payment);

            mockMvc.perform(post("/booking/add-booking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingRequestDTO)))
                .andExpect(status().isCreated());
    
           }

    @Test
    void testGetAllBookings() throws Exception {
        // Fetch all bookings
        mockMvc.perform(get("/booking/get-all"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateBookingByEmailAndRoomNumber() throws Exception {
        // Update check-out date
        bookingRequestDTO.setBookingStatus(BookingStatus.CANCELLED);

        mockMvc.perform(put("/booking/update-booking")
                .param("email", email)
                .param("roomNumber",roomNumber)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingRequestDTO)))
                .andExpect(status().isOk())
               .andExpect(jsonPath("$.message").value("Booking updated successfully"))
               .andExpect(jsonPath("$.status").value(200));
     }
}*/
