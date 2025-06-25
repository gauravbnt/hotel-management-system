package com.example.hms.hotel_management_system.controller;

import com.example.hms.hotel_management_system.dto.request.BookingRequestDTO;
import com.example.hms.hotel_management_system.dto.request.PaymentRequestDTO;
import com.example.hms.hotel_management_system.dto.response.BookingResponseDTO;
import com.example.hms.hotel_management_system.dto.response.PaymentResponseDTO;
import com.example.hms.hotel_management_system.enums.BookingStatus;
import com.example.hms.hotel_management_system.enums.PaymentMethod;
import com.example.hms.hotel_management_system.enums.RoomType;
import com.example.hms.hotel_management_system.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    private BookingRequestDTO getBookingRequestDTO() {
        PaymentRequestDTO payment = new PaymentRequestDTO( new BigDecimal("5000"),PaymentMethod.UPI, "TXN1234","A101","gaurav@example.com");

        return new BookingRequestDTO(
                Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().plusDays(2)),
                "gaurav@example.com",
                "101",
                RoomType.SINGLE,
                BookingStatus.BOOKED,
                new BigDecimal(5000),
                payment
        );
    }

    private BookingResponseDTO getBookingResponseDTO() {
        return new BookingResponseDTO(
                UUID.randomUUID(),
                Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().plusDays(2)),
                BookingStatus.BOOKED,
                new BigDecimal("5000"),
                "gaurav@example.com",
                "A101",
                true,
                Timestamp.valueOf(LocalDateTime.now()),
                RoomType.SINGLE,
                new PaymentResponseDTO(UUID.randomUUID(),Timestamp.valueOf(LocalDateTime.now()) ,new BigDecimal("5000"),PaymentMethod.UPI, "TXN1234","A101","gaurav@example.com")
        );
    }

    @Test
    void testAddBooking() throws Exception {
        BookingRequestDTO request = getBookingRequestDTO();
        BookingResponseDTO response = getBookingResponseDTO();

        Mockito.when(bookingService.createBooking(any(BookingRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/booking/add-booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Booking created successfully"))
                .andExpect(jsonPath("$.data.email").value("gaurav@example.com"));
    }

    @Test
    void testGetAllBookings() throws Exception {
        List<BookingResponseDTO> responseList = Arrays.asList(getBookingResponseDTO(), getBookingResponseDTO());

        Mockito.when(bookingService.getAllBookings()).thenReturn(responseList);

        mockMvc.perform(get("/booking/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("All bookings fetched successfully"))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void testUpdateBooking() throws Exception {
        BookingRequestDTO request = getBookingRequestDTO();
        BookingResponseDTO response = getBookingResponseDTO();

        Mockito.when(bookingService.updateBookingByRoomNumberAndEmail(eq("A101"), eq("gaurav@example.com"), any(BookingRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(put("/booking/update-booking")
                        .param("roomNumber", "A101")
                        .param("email", "gaurav@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Booking updated successfully"))
                .andExpect(jsonPath("$.data.roomNumber").value("A101"));
    }
}
