package com.example.hms.hotel_management_system.service.Impl;

import org.springframework.stereotype.Service;

import com.example.hms.hotel_management_system.dto.PaymentDTO;
import com.example.hms.hotel_management_system.entity.Booking;
import com.example.hms.hotel_management_system.entity.Payment;
import com.example.hms.hotel_management_system.exception.BookingNotFoundException;
import com.example.hms.hotel_management_system.repository.BookingRepository;
import com.example.hms.hotel_management_system.repository.PaymentRepository;
import com.example.hms.hotel_management_system.service.PaymentService;
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository,
                                BookingRepository bookingRepository){
        this.paymentRepository=paymentRepository;
        this.bookingRepository=bookingRepository;
    }

    @Override
    public Payment createPayment(PaymentDTO paymentDTO){
        Booking booking=bookingRepository.findByRoom_RoomNumberAndGuest_Email(paymentDTO.getRoomNumber(),paymentDTO.getEmail());
        
        if (booking == null) {
            throw new BookingNotFoundException("Booking not found for room " + paymentDTO.getRoomNumber() + " and email " + paymentDTO.getEmail());
        }

        Payment payment = new Payment();
        payment.setAmountPaid(paymentDTO.getAmountPaid());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setTransactionId(paymentDTO.getTransactionId());
        payment.setBooking(booking);

        booking.setPayment(payment);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentByTransactionId(String transactionId){
        return paymentRepository.findByTransactionId(transactionId);
    }

    @Override
    public Payment updatePaymentByTransactionId(Payment payment,String transactionId){
        Payment update= getPaymentByTransactionId(transactionId);
        update.setAmountPaid(payment.getAmountPaid());
        update.setPaymentMethod(payment.getPaymentMethod());
        return paymentRepository.save(update);
    }

}
