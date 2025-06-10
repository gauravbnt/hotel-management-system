package com.example.hms.hotel_management_system.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.hms.hotel_management_system.dto.PaymentRequestDTO;
import com.example.hms.hotel_management_system.dto.PaymentResponseDTO;
import com.example.hms.hotel_management_system.entity.Booking;
import com.example.hms.hotel_management_system.entity.Payment;
import com.example.hms.hotel_management_system.exception.BookingNotFoundException;
import com.example.hms.hotel_management_system.exception.PaymentAlreadyExistsException;
import com.example.hms.hotel_management_system.exception.PaymentNotFoundException;
import com.example.hms.hotel_management_system.mapper.PaymentMapper;
import com.example.hms.hotel_management_system.repository.BookingRepository;
import com.example.hms.hotel_management_system.repository.PaymentRepository;
import com.example.hms.hotel_management_system.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentMapper paymentMapper;


    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              BookingRepository bookingRepository,
                              PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.paymentMapper=paymentMapper;
    }

    private String generateTransactionId() {
        int random = new java.util.Random().nextInt(9000) + 1000;
        return "TXN" + random;
    }

    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO) {
        logger.info("Creating payment for room: {} and email: {}", paymentRequestDTO.getRoomNumber(), paymentRequestDTO.getEmail());
        Booking booking = bookingRepository.findByRoom_RoomNumberAndGuest_Email(
                paymentRequestDTO.getRoomNumber(), paymentRequestDTO.getEmail());

        if (booking == null) {
            logger.error("Booking not found for room: {} and email: {}", paymentRequestDTO.getRoomNumber(), paymentRequestDTO.getEmail());
            throw new BookingNotFoundException(
                    "Booking not found for room " + paymentRequestDTO.getRoomNumber() + " and email " + paymentRequestDTO.getEmail());
        }

        if (booking.getPayment() != null) {
            logger.warn("Payment already exists for booking: room {}, email {}", paymentRequestDTO.getRoomNumber(), paymentRequestDTO.getEmail());
            throw new PaymentAlreadyExistsException("Payment has already been made for this booking.");
        }
        Payment p1=paymentMapper.toEntity(paymentRequestDTO);
        Payment payment = new Payment();
        payment.setAmountPaid(p1.getAmountPaid());
        payment.setPaymentMethod(p1.getPaymentMethod());
        String transactionId = generateTransactionId();
        payment.setTransactionId(transactionId);
        payment.setBooking(booking);

        booking.setPayment(payment);
        Payment savedPayment = paymentRepository.saveAndFlush(payment);

        logger.info("Payment created successfully with transaction ID: {}", transactionId);
        return paymentMapper.toResponseDTO(savedPayment);
    }

    @Override
    public PaymentResponseDTO getPaymentByTransactionId(String transactionId) {
        logger.info("Retrieving payment with transaction ID: {}", transactionId);
        Payment payment = paymentRepository.findByTransactionId(transactionId);
        if (payment == null) {
            logger.error("Payment not found with transaction ID: {}", transactionId);
            throw new PaymentNotFoundException("Payment not found with the id" + transactionId);
        }
        logger.info("Payment retrieved successfully for transaction ID: {}", transactionId);
        return paymentMapper.toResponseDTO(payment);
    }

    @Override
    public PaymentResponseDTO updatePaymentByTransactionId(PaymentRequestDTO paymentRequestDTO, String transactionId) {
        logger.info("Updating payment for transaction ID: {}", transactionId);
        Payment update = paymentRepository.findByTransactionId(transactionId);
        if (update == null) {
            logger.error("Payment not found for transaction ID: {}", transactionId);
            throw new PaymentNotFoundException("Payment not found with the id" + transactionId);
        }
        Payment payment=paymentMapper.toEntity(paymentRequestDTO);
        update.setAmountPaid(payment.getAmountPaid());
        update.setPaymentMethod(payment.getPaymentMethod());

        Payment updatedPayment = paymentRepository.save(update);
        logger.info("Payment updated successfully for transaction ID: {}", transactionId);
        return paymentMapper.toResponseDTO(updatedPayment);
    }
}
