package com.example.hms.hotel_management_system.service.impl;

import com.example.hms.hotel_management_system.dto.request.PaymentRequestDTO;
import com.example.hms.hotel_management_system.dto.response.PaymentResponseDTO;
import com.example.hms.hotel_management_system.entity.Booking;
import com.example.hms.hotel_management_system.entity.Payment;
import com.example.hms.hotel_management_system.enums.PaymentMethod;
import com.example.hms.hotel_management_system.exception.BookingNotFoundException;
import com.example.hms.hotel_management_system.exception.PaymentAlreadyExistsException;
import com.example.hms.hotel_management_system.exception.PaymentNotFoundException;
import com.example.hms.hotel_management_system.mapper.PaymentMapper;
import com.example.hms.hotel_management_system.repository.BookingRepository;
import com.example.hms.hotel_management_system.repository.PaymentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private PaymentRequestDTO paymentRequestDTO;
    private Payment payment;
    private Booking booking;
    private PaymentResponseDTO paymentResponseDTO;

    @BeforeEach
    void setUp() {
        // Initialize response DTO
        paymentResponseDTO = new PaymentResponseDTO();
        paymentResponseDTO.setTransactionId("TXN1234");
        paymentResponseDTO.setAmountPaid(BigDecimal.valueOf(1000.0));
        paymentResponseDTO.setPaymentMethod(PaymentMethod.CARD);

        // Initialize request DTO
        paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setRoomNumber("101");
        paymentRequestDTO.setEmail("test@example.com");
        paymentRequestDTO.setAmountPaid(paymentResponseDTO.getAmountPaid());
        paymentRequestDTO.setPaymentMethod(paymentResponseDTO.getPaymentMethod());

        // Initialize payment entity
        payment = new Payment();
        payment.setAmountPaid(paymentResponseDTO.getAmountPaid());
        payment.setPaymentMethod(paymentResponseDTO.getPaymentMethod());

        // Initialize booking
        booking = new Booking();
        booking.setRoom(null); // You can mock Room if needed
        booking.setGuest(null); // You can mock Guest if needed
    }

    @Test
    void testCreatePayment_Success() {
        when(bookingRepository.findByRoom_RoomNumberAndGuest_Email("101", "test@example.com")).thenReturn(booking);
        when(paymentMapper.toEntity(paymentRequestDTO)).thenReturn(payment);
        when(paymentRepository.saveAndFlush(any(Payment.class))).thenAnswer(invocation -> {
            Payment saved = invocation.getArgument(0);
            saved.setTransactionId("TXN1234");
            return saved;
        });
        when(paymentMapper.toResponseDTO(any(Payment.class))).thenReturn(paymentResponseDTO);

        PaymentResponseDTO result = paymentService.createPayment(paymentRequestDTO);

        assertNotNull(result);
        assertEquals("TXN1234", result.getTransactionId());
        verify(paymentRepository, times(1)).saveAndFlush(any(Payment.class));
    }

    @Test
    void testCreatePayment_BookingNotFound() {
        when(bookingRepository.findByRoom_RoomNumberAndGuest_Email("101", "test@example.com")).thenReturn(null);

        assertThrows(BookingNotFoundException.class, () -> paymentService.createPayment(paymentRequestDTO));
    }

    @Test
    void testCreatePayment_AlreadyExists() {
        booking.setPayment(new Payment());
        when(bookingRepository.findByRoom_RoomNumberAndGuest_Email("101", "test@example.com")).thenReturn(booking);

        assertThrows(PaymentAlreadyExistsException.class, () -> paymentService.createPayment(paymentRequestDTO));
    }

    @Test
    void testGetPaymentByTransactionId_Success() {
        when(paymentRepository.findByTransactionId("TXN1234")).thenReturn(payment);
        when(paymentMapper.toResponseDTO(payment)).thenReturn(paymentResponseDTO);

        PaymentResponseDTO result = paymentService.getPaymentByTransactionId("TXN1234");

        assertNotNull(result);
        assertEquals("TXN1234", result.getTransactionId());
    }

    @Test
    void testGetPaymentByTransactionId_NotFound() {
        when(paymentRepository.findByTransactionId("TXN9999")).thenReturn(null);

        assertThrows(PaymentNotFoundException.class, () -> paymentService.getPaymentByTransactionId("TXN9999"));
    }

    @Test
    void testUpdatePaymentByTransactionId_Success() {
        when(paymentRepository.findByTransactionId("TXN1234")).thenReturn(payment);
        when(paymentMapper.toEntity(paymentRequestDTO)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.toResponseDTO(payment)).thenReturn(paymentResponseDTO);

        PaymentResponseDTO result = paymentService.updatePaymentByTransactionId(paymentRequestDTO, "TXN1234");

        assertNotNull(result);
        assertEquals("TXN1234", result.getTransactionId());
    }

    @Test
    void testUpdatePaymentByTransactionId_NotFound() {
        when(paymentRepository.findByTransactionId("TXN1234")).thenReturn(null);

        assertThrows(PaymentNotFoundException.class, () -> paymentService.updatePaymentByTransactionId(paymentRequestDTO, "TXN1234"));
    }
}
