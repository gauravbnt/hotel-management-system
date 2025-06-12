package com.example.hms.hotel_management_system.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.hms.hotel_management_system.response.ErrorResponse;
import com.example.hms.hotel_management_system.response.SuccessResponse;
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
        this.paymentMapper = paymentMapper;
    }

    private String generateTransactionId() {
        int random = new java.util.Random().nextInt(9000) + 1000;
        return "TXN" + random;
    }

    @Override
    public ResponseEntity<?> createPayment(PaymentRequestDTO paymentRequestDTO) {
        try {
            logger.info("Creating payment for room: {} and email: {}", paymentRequestDTO.getRoomNumber(),
                    paymentRequestDTO.getEmail());
            Booking booking = bookingRepository.findByRoom_RoomNumberAndGuest_Email(
                    paymentRequestDTO.getRoomNumber(), paymentRequestDTO.getEmail());

            if (booking == null) {
                logger.error("Booking not found for room: {} and email: {}", paymentRequestDTO.getRoomNumber(),
                        paymentRequestDTO.getEmail());
                throw new BookingNotFoundException(
                        "Booking not found for room " + paymentRequestDTO.getRoomNumber() + " and email "
                                + paymentRequestDTO.getEmail());
            }

            if (booking.getPayment() != null) {
                logger.warn("Payment already exists for booking: room {}, email {}", paymentRequestDTO.getRoomNumber(),
                        paymentRequestDTO.getEmail());
                throw new PaymentAlreadyExistsException("Payment has already been made for this booking.");
            }

            Payment payment = new Payment();
            payment.setAmountPaid(paymentRequestDTO.getAmountPaid());
            payment.setPaymentMethod(paymentRequestDTO.getPaymentMethod());
            String transactionId = generateTransactionId();
            payment.setTransactionId(transactionId);
            payment.setBooking(booking);

            booking.setPayment(payment);
            Payment savedPayment = paymentRepository.saveAndFlush(payment);

            logger.info("Payment created successfully with transaction ID: {}", transactionId);
            PaymentResponseDTO responseDTO = paymentMapper.toResponseDTO(savedPayment);
            SuccessResponse<PaymentResponseDTO> response = new SuccessResponse<>(
                    "Payment created successfully",
                    HttpStatus.OK.value(),
                    responseDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (PaymentAlreadyExistsException | BookingNotFoundException e) {
            logger.error("Payment unsuccessful: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Payment not successful..", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getPaymentByTransactionId(String transactionId) {
        try {
            logger.info("Retrieving payment with transaction ID: {}", transactionId);
            Payment payment = paymentRepository.findByTransactionId(transactionId);
            if (payment == null) {
                logger.error("Payment not found with transaction ID: {}", transactionId);
                throw new PaymentNotFoundException("Payment not found with the id" + transactionId);
            }
            logger.info("Payment retrieved successfully for transaction ID: {}", transactionId);
            PaymentResponseDTO responseDTO = paymentMapper.toResponseDTO(payment);
            SuccessResponse<PaymentResponseDTO> response = new SuccessResponse<>(
                    "Payment fetched successfully",
                    HttpStatus.OK.value(),
                    responseDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (PaymentNotFoundException e) {
            logger.error("Payment not found: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Payment not found ", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> updatePaymentByTransactionId(PaymentRequestDTO paymentRequestDTO, String transactionId) {
        try {
            logger.info("Updating payment for transaction ID: {}", transactionId);
            Payment update = paymentRepository.findByTransactionId(transactionId);
            if (update == null) {
                logger.error("Payment not found for transaction ID: {}", transactionId);
                throw new PaymentNotFoundException("Payment not found with the id" + transactionId);
            }

            update.setAmountPaid(paymentRequestDTO.getAmountPaid());
            update.setPaymentMethod(paymentRequestDTO.getPaymentMethod());

            Payment updatedPayment = paymentRepository.save(update);
            logger.info("Payment updated successfully for transaction ID: {}", transactionId);
            PaymentResponseDTO responseDTO = paymentMapper.toResponseDTO(updatedPayment);

            SuccessResponse<PaymentResponseDTO> response = new SuccessResponse<>(
                    "Payment updated successfully",
                    HttpStatus.OK.value(),
                    responseDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (PaymentNotFoundException e) {
            logger.error("Payment not found: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Payment not found", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse("Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
