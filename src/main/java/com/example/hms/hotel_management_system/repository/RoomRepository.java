package com.example.hms.hotel_management_system.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hms.hotel_management_system.entity.Room;
import com.example.hms.hotel_management_system.enums.RoomType;

@Repository
public interface RoomRepository extends JpaRepository<Room,UUID>{
    Room findRoomByRoomNumber(String roomNumber);
    List<Room> findByIsAvailable(Boolean isAvailable);
    List<Room> findByRoomTypeAndIsAvailableTrue(RoomType roomType);

}
