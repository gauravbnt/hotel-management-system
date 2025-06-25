package com.example.hotelmanagementsystem.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import com.example.hotelmanagementsystem.dto.request.BookingRequestDTO;
import com.example.hotelmanagementsystem.dto.request.PaymentRequestDTO;
import com.example.hotelmanagementsystem.dto.response.BookingResponseDTO;
import com.example.hotelmanagementsystem.entity.*;
import com.example.hotelmanagementsystem.enums.BookingStatus;
import com.example.hotelmanagementsystem.enums.RoomType;
import com.example.hotelmanagementsystem.exception.*;
import com.example.hotelmanagementsystem.mapper.BookingMapper;
import com.example.hotelmanagementsystem.repository.BookingRepository;
import com.example.hotelmanagementsystem.repository.GuestRepository;
import com.example.hotelmanagementsystem.repository.RoomRepository;
import com.example.hotelmanagementsystem.service.PaymentService;
import com.example.hotelmanagementsystem.service.impl.BookingServiceImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private RoomRepository roomRepository;
    @Mock private GuestRepository guestRepository;
    @Mock private BookingMapper bookingMapper;
    @Mock private PaymentService paymentService;

    @InjectMocks private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Timestamp getFutureTimestamp(int days) {
        return new Timestamp(System.currentTimeMillis() + days * 24L * 60 * 60 * 1000);
    }

    @Test
    void testCreateBooking_Success() {
        BookingRequestDTO bookingRequest = new BookingRequestDTO(); 
        bookingRequest.setEmail("test@example.com");
        bookingRequest.setRoomType(RoomType.SINGLE);
        bookingRequest.setCheckInDate(getFutureTimestamp(1));
        bookingRequest.setCheckOutDate(getFutureTimestamp(3));

        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        bookingRequest.setPayment(paymentRequest);

        Guest guest = new Guest();
        Room room = new Room();
        room.setRoomNumber("101");
        room.setPricePerNight(BigDecimal.valueOf(1000));
        room.setIsAvailable(true);

        List<Room> availableRooms = List.of(room);
        Booking booking = new Booking();
        booking.setGuest(guest);
        booking.setRoom(room);
        booking.setCheckInDate(bookingRequest.getCheckInDate());
        booking.setCheckOutDate(bookingRequest.getCheckOutDate());

        Booking savedBooking = new Booking();
        savedBooking.setRoom(room);
        savedBooking.setGuest(guest);
        savedBooking.setCheckInDate(bookingRequest.getCheckInDate());
        savedBooking.setCheckOutDate(bookingRequest.getCheckOutDate());

        when(roomRepository.findByRoomTypeAndIsAvailableTrue(RoomType.SINGLE)).thenReturn(availableRooms);
        when(guestRepository.findByEmail("test@example.com")).thenReturn(guest);
        when(bookingMapper.toEntity(bookingRequest)).thenReturn(booking);
        when(bookingRepository.findByRoom(room)).thenReturn(Collections.emptyList());
        when(bookingRepository.save(any())).thenReturn(savedBooking);
        when(bookingMapper.toResponseDTO(savedBooking)).thenReturn(new BookingResponseDTO());

        BookingResponseDTO result = bookingService.createBooking(bookingRequest);

        assertNotNull(result);
        verify(paymentService).createPayment(any(PaymentRequestDTO.class));
    }

    @Test
    void testCreateBooking_NoAvailableRooms() {
        BookingRequestDTO bookingRequest = new BookingRequestDTO();
        bookingRequest.setRoomType(RoomType.SINGLE);
        bookingRequest.setEmail("test@example.com");
        bookingRequest.setCheckInDate(getFutureTimestamp(1));
        bookingRequest.setCheckOutDate(getFutureTimestamp(2));
        bookingRequest.setPayment(new PaymentRequestDTO());

        when(roomRepository.findByRoomTypeAndIsAvailableTrue(RoomType.SINGLE)).thenReturn(Collections.emptyList());
        when(guestRepository.findByEmail("test@example.com")).thenReturn(new Guest());

        assertThrows(RoomAlreadyBookedException.class, () -> bookingService.createBooking(bookingRequest));
    }

    @Test
    void testCreateBooking_InvalidDates() {
        BookingRequestDTO bookingRequest = new BookingRequestDTO();
        bookingRequest.setEmail("test@example.com");
        bookingRequest.setCheckInDate(getFutureTimestamp(3));
        bookingRequest.setCheckOutDate(getFutureTimestamp(1));
        bookingRequest.setPayment(new PaymentRequestDTO());

        when(roomRepository.findByRoomTypeAndIsAvailableTrue(RoomType.SINGLE)).thenReturn(List.of(new Room()));
        when(guestRepository.findByEmail("test@example.com")).thenReturn(new Guest());

        assertThrows(InvalidBookingDateException.class, () -> bookingService.createBooking(bookingRequest));
    }

    @Test
    void testCreateBooking_GuestNotFound() {
        BookingRequestDTO bookingRequest = new BookingRequestDTO();
        bookingRequest.setEmail("test@example.com");

        when(guestRepository.findByEmail("test@example.com")).thenReturn(null);

        assertThrows(BookingNotFoundException.class, () -> bookingService.createBooking(bookingRequest));
    }

@Test
void testCreateBooking_PaymentMissing() {
    // Arrange
    BookingRequestDTO bookingRequestDTO = new BookingRequestDTO();
    bookingRequestDTO.setEmail("test@example.com");
    bookingRequestDTO.setRoomType(RoomType.SINGLE);
    bookingRequestDTO.setCheckInDate(Timestamp.valueOf("2025-06-20 14:00:00"));
    bookingRequestDTO.setCheckOutDate(Timestamp.valueOf("2025-06-22 11:00:00"));
    bookingRequestDTO.setPayment(null); // Missing payment info
    when(bookingMapper.toEntity(any(BookingRequestDTO.class))).thenReturn(new Booking());

    Room room = new Room();
    room.setRoomNumber("101");
    room.setPricePerNight(BigDecimal.valueOf(2000));
    room.setIsAvailable(true);

    Guest guest = new Guest();
    guest.setEmail("test@example.com");

    when(guestRepository.findByEmail("test@example.com")).thenReturn(guest);
    when(roomRepository.findByRoomTypeAndIsAvailableTrue(RoomType.SINGLE))
        .thenReturn(List.of(room));
    when(bookingRepository.findByRoom(any(Room.class))).thenReturn(List.of());

    // Act & Assert
    assertThrows(PaymentInformationIsNullException.class, () -> {
        bookingService.createBooking(bookingRequestDTO);
    });
}

    @Test
    void testGetAllBookings_EmptyList() {
        when(bookingRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(BookingNotFoundException.class, () -> bookingService.getAllBookings());
    }

    @Test
    void testUpdateBookingByRoomNumberAndEmail_CancelBooking() {
        BookingRequestDTO bookingRequest = new BookingRequestDTO();
        bookingRequest.setBookingStatus(BookingStatus.CANCELLED);

        Booking booking = new Booking();
        booking.setRoom(new Room());
        booking.setGuest(new Guest());
        booking.setBookingStatus(BookingStatus.BOOKED);

        when(bookingRepository.findByRoom_RoomNumberAndGuest_Email("101", "test@example.com")).thenReturn(booking);
        when(bookingMapper.toResponseDTO(any())).thenReturn(new BookingResponseDTO());

        BookingResponseDTO response = bookingService.updateBookingByRoomNumberAndEmail("101", "test@example.com", bookingRequest);
        assertNotNull(response);
        verify(roomRepository).save(any());
        verify(bookingRepository).save(any());
    }

    @Test
    void testUpdateBookingByRoomNumberAndEmail_NotFound() {
        when(bookingRepository.findByRoom_RoomNumberAndGuest_Email("101", "test@example.com")).thenReturn(null);
        assertThrows(BookingNotFoundException.class,
                () -> bookingService.updateBookingByRoomNumberAndEmail("101", "test@example.com", new BookingRequestDTO()));
    }

    @Test
    void testCalculateTotalAmount_InvalidPrice() throws Exception {
        // Arrange
        Room room = new Room();
        room.setPricePerNight(null); // Missing price

        Timestamp checkIn = Timestamp.valueOf("2025-06-20 14:00:00");
        Timestamp checkOut = Timestamp.valueOf("2025-06-21 11:00:00");

        // Access private method using reflection
        Method method = BookingServiceImpl.class.getDeclaredMethod("calculateTotalAmount", Room.class, Timestamp.class, Timestamp.class);
        method.setAccessible(true);

        // Act & Assert
        assertThrows(InvocationTargetException.class, () -> {
            method.invoke(bookingService, room, checkIn, checkOut);
        }, "Expected InvocationTargetException due to IllegalStateException inside private method");
    }
}
