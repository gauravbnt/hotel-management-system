package com.example.hms.hotel_management_system.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.hms.hotel_management_system.dto.PaymentDTO;
import com.example.hms.hotel_management_system.entity.Booking;
import com.example.hms.hotel_management_system.entity.Payment;
import com.example.hms.hotel_management_system.exception.BookingNotFoundException;
import com.example.hms.hotel_management_system.exception.PaymentAlreadyExistsException;
import com.example.hms.hotel_management_system.exception.PaymentNotFoundException;
import com.example.hms.hotel_management_system.repository.BookingRepository;
import com.example.hms.hotel_management_system.repository.PaymentRepository;
import com.example.hms.hotel_management_system.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
    }

    private String generateTransactionId() {
        int random = new java.util.Random().nextInt(9000) + 1000;
        return "TXN" + random;
    }

    @Override
    public Payment createPayment(PaymentDTO paymentDTO) {
        logger.info("Creating payment for room: {} and email: {}", paymentDTO.getRoomNumber(), paymentDTO.getEmail());

        Booking booking = bookingRepository.findByRoom_RoomNumberAndGuest_Email(
                paymentDTO.getRoomNumber(), paymentDTO.getEmail());

        if (booking == null) {
            logger.error("Booking not found for room: {} and email: {}", paymentDTO.getRoomNumber(), paymentDTO.getEmail());
            throw new BookingNotFoundException(
                    "Booking not found for room " + paymentDTO.getRoomNumber() + " and email " + paymentDTO.getEmail());
        }

        if (booking.getPayment() != null) {
            logger.warn("Payment already exists for booking: room {}, email {}", paymentDTO.getRoomNumber(), paymentDTO.getEmail());
            throw new PaymentAlreadyExistsException("Payment has already been made for this booking.");
        }

        Payment payment = new Payment();
        payment.setAmountPaid(paymentDTO.getAmountPaid());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        String transactionId = generateTransactionId();
        payment.setTransactionId(transactionId);
        payment.setBooking(booking);

        booking.setPayment(payment);
        Payment savedPayment = paymentRepository.saveAndFlush(payment);

        logger.info("Payment created successfully with transaction ID: {}", transactionId);
        return savedPayment;
    }

    @Override
    public Payment getPaymentByTransactionId(String transactionId) {
        logger.info("Retrieving payment with transaction ID: {}", transactionId);
        Payment payment = paymentRepository.findByTransactionId(transactionId);
        if (payment == null) {
            logger.error("Payment not found with transaction ID: {}", transactionId);
            throw new PaymentNotFoundException("Payment not found with the id" + transactionId);
        }
        logger.info("Payment retrieved successfully for transaction ID: {}", transactionId);
        return payment;
    }

    @Override
    public Payment updatePaymentByTransactionId(Payment payment, String transactionId) {
        logger.info("Updating payment for transaction ID: {}", transactionId);
        Payment update = getPaymentByTransactionId(transactionId);
        if (update == null) {
            logger.error("Payment not found for transaction ID: {}", transactionId);
            throw new PaymentNotFoundException("Payment not found with the id" + transactionId);
        }

        update.setAmountPaid(payment.getAmountPaid());
        update.setPaymentMethod(payment.getPaymentMethod());
        Payment updatedPayment = paymentRepository.save(update);
        logger.info("Payment updated successfully for transaction ID: {}", transactionId);
        return updatedPayment;
    }
}
