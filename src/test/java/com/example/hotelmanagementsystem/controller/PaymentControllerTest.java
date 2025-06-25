package com.example.hotelmanagementsystem.controller;

import com.example.hotelmanagementsystem.dto.request.PaymentRequestDTO;
import com.example.hotelmanagementsystem.dto.response.PaymentResponseDTO;
import com.example.hotelmanagementsystem.enums.PaymentMethod;
import com.example.hotelmanagementsystem.service.PaymentService;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    private PaymentRequestDTO getPaymentRequest() {
        return new PaymentRequestDTO(
                new BigDecimal("2500.00"),
                PaymentMethod.CARD,
                "TXN5678",
                "B201",
                "testuser@example.com"
        );
    }

    private PaymentResponseDTO getPaymentResponse() {
        return new PaymentResponseDTO(
                UUID.randomUUID(),
                Timestamp.valueOf(LocalDateTime.now()),
                new BigDecimal("2500.00"),
                PaymentMethod.CARD,
                "TXN5678",
                "B201",
                "testuser@example.com"
        );
    }

    @Test
    void testCreatePayment() throws Exception {
        PaymentRequestDTO request = getPaymentRequest();
        PaymentResponseDTO response = getPaymentResponse();

        Mockito.when(paymentService.createPayment(any(PaymentRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/payment/add-payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Payment created successfully"))
                .andExpect(jsonPath("$.data.transactionId").value("TXN5678"));
    }

    @Test
    void testGetPaymentByTransactionId() throws Exception {
        String transactionId = "TXN5678";
        PaymentResponseDTO response = getPaymentResponse();

        Mockito.when(paymentService.getPaymentByTransactionId(transactionId)).thenReturn(response);

        mockMvc.perform(get("/payment/get-payment-by-trans-id/" + transactionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Payment fetched successfully"))
                .andExpect(jsonPath("$.data.transactionId").value("TXN5678"));
    }

    @Test
    void testUpdatePaymentByTransactionId() throws Exception {
        String transactionId = "TXN5678";
        PaymentRequestDTO request = getPaymentRequest();
        PaymentResponseDTO response = getPaymentResponse();

        Mockito.when(paymentService.updatePaymentByTransactionId(eq(request), eq(transactionId)))
                .thenReturn(response);

        mockMvc.perform(put("/payment/update-payment/" + transactionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Payment updated successfully"))
                .andExpect(jsonPath("$.data.transactionId").value("TXN5678"));
    }
}
