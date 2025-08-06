package com.example.hotelmanagementsystem.controller.integration;

import com.example.hotelmanagementsystem.dto.request.GuestRequestDTO;
import com.example.hotelmanagementsystem.dto.request.PaymentRequestDTO;
import com.example.hotelmanagementsystem.dto.request.RoomRequestDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PaymentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private PaymentRequestDTO paymentRequest;
    private final String email = "gaurav@example.com";
    private final String roomNumber = "101";
    private final String transactionId = "TXN1234";

    @BeforeEach
    void setUp() throws Exception {
        // Create guest
        GuestRequestDTO guest = new GuestRequestDTO();
        guest.setEmail(email);
        guest.setFirstName("Gaurav");
        guest.setLastName("Chopada");
        guest.setPhoneNumber("9999999999");
        guest.setAddress("Mumbai");

        mockMvc.perform(post("/guest/add-guest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(guest)))
                .andExpect(status().isCreated());

        // Create room
        RoomRequestDTO room = new RoomRequestDTO(
                roomNumber, RoomType.SINGLE,
                BigDecimal.valueOf(1000),
                true, 1, "First floor"
        );

        mockMvc.perform(post("/room/add-room")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isCreated());

        // Prepare payment DTO
        paymentRequest = new PaymentRequestDTO();
        paymentRequest.setEmail(email);
        paymentRequest.setRoomNumber(roomNumber);
        paymentRequest.setAmountPaid(BigDecimal.valueOf(2000));
        paymentRequest.setPaymentMethod(PaymentMethod.CASH);
        paymentRequest.setTransactionId(transactionId);

        // Create initial payment
        mockMvc.perform(post("/payment/add-payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreatePayment() throws Exception {
        // New payment with different transaction ID
        PaymentRequestDTO newPayment = new PaymentRequestDTO();
        newPayment.setEmail(email);
        newPayment.setRoomNumber(roomNumber);
        newPayment.setAmountPaid(BigDecimal.valueOf(2500));
        newPayment.setPaymentMethod(PaymentMethod.UPI);
        newPayment.setTransactionId("TXN9999");

        mockMvc.perform(post("/payment/add-payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPayment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Payment created successfully"))
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.data.roomNumber").value(roomNumber))
                .andExpect(jsonPath("$.data.transactionId").value("TXN9999"));
    }

    @Test
    void testGetPaymentByTransactionId() throws Exception {
        mockMvc.perform(get("/payment/get-payment-by-trans-id/{transactionId}", transactionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Payment fetched successfully"))
                .andExpect(jsonPath("$.data.transactionId").value(transactionId));
    }

    @Test
    void testUpdatePaymentByTransactionId() throws Exception {
        paymentRequest.setAmountPaid(BigDecimal.valueOf(3000));

        mockMvc.perform(put("/payment/update-payment/{transactionId}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Payment updated successfully"))
                .andExpect(jsonPath("$.data.transactionId").value(transactionId))
                .andExpect(jsonPath("$.data.amountPaid").value(3000));
    }
}
*/