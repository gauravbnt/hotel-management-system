package com.example.hms.hotel_management_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.hms.hotel_management_system.DTO.BookingDTO;
import com.example.hms.hotel_management_system.entity.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {


    @Mapping(source = "guest.email", target = "email")
    @Mapping(source = "room.roomNumber", target = "roomNumber")
    BookingDTO toDTO(Booking booking);

    @Mapping(target = "guest", ignore = true) 
    @Mapping(target = "room", ignore = true)  
    Booking toEntity(BookingDTO bookingDTO);
}
