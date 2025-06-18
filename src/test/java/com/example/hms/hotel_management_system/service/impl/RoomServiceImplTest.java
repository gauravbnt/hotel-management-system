package com.example.hms.hotel_management_system.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.hms.hotel_management_system.dto.request.RoomRequestDTO;
import com.example.hms.hotel_management_system.dto.response.RoomResponseDTO;
import com.example.hms.hotel_management_system.entity.Room;
import com.example.hms.hotel_management_system.exception.RoomAlreadyExistsException;
import com.example.hms.hotel_management_system.exception.RoomNotFoundException;
import com.example.hms.hotel_management_system.mapper.RoomMapper;
import com.example.hms.hotel_management_system.repository.RoomRepository;

public class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomServiceImpl roomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRoom_Success() {
        RoomRequestDTO dto = new RoomRequestDTO();
        Room room = new Room();
        Room savedRoom = new Room();
        RoomResponseDTO responseDTO = new RoomResponseDTO();

        when(roomMapper.toEntity(dto)).thenReturn(room);
        when(roomRepository.existsByRoomNumber(room.getRoomNumber())).thenReturn(false);
        when(roomRepository.save(room)).thenReturn(savedRoom);
        when(roomMapper.toResponseDTO(savedRoom)).thenReturn(responseDTO);

        RoomResponseDTO result = roomService.createRoom(dto);
        assertEquals(responseDTO, result);
    }

    @Test
    void testCreateRoom_RoomAlreadyExists() {
        RoomRequestDTO dto = new RoomRequestDTO();
        Room room = new Room();

        when(roomMapper.toEntity(dto)).thenReturn(room);
        when(roomRepository.existsByRoomNumber(room.getRoomNumber())).thenReturn(true);

        assertThrows(RoomAlreadyExistsException.class, () -> roomService.createRoom(dto));
    }

    @Test
    void testGetRoomByRoomNumber_Success() {
        String roomNumber = "101";
        Room room = new Room();
        RoomResponseDTO responseDTO = new RoomResponseDTO();

        when(roomRepository.findRoomByRoomNumber(roomNumber)).thenReturn(room);
        when(roomMapper.toResponseDTO(room)).thenReturn(responseDTO);

        RoomResponseDTO result = roomService.getRoomByRoomNumber(roomNumber);
        assertEquals(responseDTO, result);
    }

    @Test
    void testGetRoomByRoomNumber_NotFound() {
        when(roomRepository.findRoomByRoomNumber("404")).thenReturn(null);
        assertThrows(RoomNotFoundException.class, () -> roomService.getRoomByRoomNumber("404"));
    }

    @Test
    void testGetAvailableRooms_Success() {
        List<Room> rooms = Arrays.asList(new Room(), new Room());
        List<RoomResponseDTO> responseList = Arrays.asList(new RoomResponseDTO(), new RoomResponseDTO());

        when(roomRepository.findByIsAvailable(true)).thenReturn(rooms);
        when(roomMapper.toResponseDTOList(rooms)).thenReturn(responseList);

        List<RoomResponseDTO> result = roomService.getAvailableRooms(true);
        assertEquals(responseList.size(), result.size());
    }

    @Test
    void testGetAvailableRooms_NullFlag() {
        assertThrows(IllegalArgumentException.class, () -> roomService.getAvailableRooms(null));
    }

    @Test
    void testGetAllRooms_Success() {
        List<Room> rooms = Arrays.asList(new Room(), new Room());
        List<RoomResponseDTO> responseList = Arrays.asList(new RoomResponseDTO(), new RoomResponseDTO());

        when(roomRepository.findAll()).thenReturn(rooms);
        when(roomMapper.toResponseDTOList(rooms)).thenReturn(responseList);

        List<RoomResponseDTO> result = roomService.getAllRooms();
        assertEquals(responseList.size(), result.size());
    }

    @Test
    void testGetAllRooms_EmptyList() {
        when(roomRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(RoomNotFoundException.class, () -> roomService.getAllRooms());
    }

    @Test
    void testUpdateRoomByRoomNumber_Success() {
        RoomRequestDTO dto = new RoomRequestDTO();
        Room existingRoom = new Room();
        Room updatedRoom = new Room();
        RoomResponseDTO responseDTO = new RoomResponseDTO();

        when(roomMapper.toEntity(dto)).thenReturn(updatedRoom);
        when(roomRepository.findRoomByRoomNumber("101")).thenReturn(existingRoom);
        when(roomRepository.save(any(Room.class))).thenReturn(updatedRoom);
        when(roomMapper.toResponseDTO(updatedRoom)).thenReturn(responseDTO);

        RoomResponseDTO result = roomService.updateRoomByRoomNumber(dto, "101");
        assertEquals(responseDTO, result);
    }
}
