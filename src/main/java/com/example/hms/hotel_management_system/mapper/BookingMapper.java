package com.example.hms.hotel_management_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.hms.hotel_management_system.dto.request.BookingRequestDTO;
import com.example.hms.hotel_management_system.dto.response.BookingResponseDTO;
import com.example.hms.hotel_management_system.entity.Booking;

@Mapper(componentModel = "spring", uses = {PaymentMapper.class})
public interface BookingMapper {


    @Mapping(source = "guest.email", target = "email")
    @Mapping(source = "room.roomNumber", target = "roomNumber")
    @Mapping(source = "booking.createdAt", target = "createdAt")
    @Mapping(source = "room.isAvailable", target = "isAvailable")
    @Mapping(source = "payment", target = "payment") 
    BookingResponseDTO toResponseDTO(Booking booking);
    

    @Mapping(target = "guest", ignore = true) 
    @Mapping(target = "room", ignore = true)  
    @Mapping(target = "payment", ignore = true)
    Booking toEntity(BookingRequestDTO bookingDTO);
}
