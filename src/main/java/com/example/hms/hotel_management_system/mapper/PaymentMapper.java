package com.example.hms.hotel_management_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.hms.hotel_management_system.dto.PaymentDTO;
import com.example.hms.hotel_management_system.entity.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {


    @Mapping(source = "booking.guest.email", target = "email")
    @Mapping(source = "booking.room.roomNumber", target = "roomNumber")
    PaymentDTO toDTO(Payment payment);

    @Mapping(target = "booking", ignore = true)
    Payment toEntity(PaymentDTO payment);
}
