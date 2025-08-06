package com.example.hotelmanagementsystem.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.hotelmanagementsystem.response.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookingNotFoundException(BookingNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
            "Booking Not Found..!",
            HttpStatus.NOT_FOUND.value()
            );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(GuestAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleGuestAlreadyExistsException(GuestAlreadyExistsException ex) {
        ErrorResponse response = new ErrorResponse(
            "Guest Already Exists..!",
            HttpStatus.CONFLICT.value()
            );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(GuestNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGuestNotFoundException(GuestNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
            "Guest Not Found..!",
            HttpStatus.NOT_FOUND.value()
            );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidBookingDateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidBookingDateException(InvalidBookingDateException ex) {
        ErrorResponse response = new ErrorResponse(
            "Booking Date is Invalid..!",
            HttpStatus.BAD_REQUEST.value()
            );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidRoomRentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRoomRentException(InvalidRoomRentException ex) {
        ErrorResponse response = new ErrorResponse(
            "Invalid Room Rent Entered..!",
            HttpStatus.BAD_REQUEST.value()
            );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlePaymentAlreadyExistsException(PaymentAlreadyExistsException ex) {
        ErrorResponse response = new ErrorResponse(
            "Payment Already Exists..!",
            HttpStatus.CONFLICT.value()
            );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PaymentInformationIsNullException.class)
    public ResponseEntity<ErrorResponse> handlePaymentInformationIsNullException(PaymentInformationIsNullException ex) {
        ErrorResponse response = new ErrorResponse(
            "Payment Information is Null..!",
            HttpStatus.BAD_REQUEST.value()
            );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentNotFoundException(PaymentNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
            "Payment Not Found..!",
            HttpStatus.NOT_FOUND.value()
            );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RoomAlreadyBookedException.class)
    public ResponseEntity<ErrorResponse> handleRoomAlreadyBookedException(RoomAlreadyBookedException ex) {
        ErrorResponse response = new ErrorResponse(
            "Room Already Booked..!",
            HttpStatus.BAD_REQUEST.value()
            );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoomAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleRoomAlreadyExistsException(RoomAlreadyExistsException ex) {
        ErrorResponse response = new ErrorResponse(
            "Room Already Exists..!",
            HttpStatus.CONFLICT.value()
            );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoomNotFoundException(RoomNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(
            "Room Not Found..!",
            HttpStatus.NOT_FOUND.value()
            );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for(FieldError error : ex.getBindingResult().getFieldErrors()){
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
