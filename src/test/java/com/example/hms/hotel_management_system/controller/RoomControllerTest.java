package com.example.hms.hotel_management_system.controller;

import com.example.hms.hotel_management_system.dto.request.RoomRequestDTO;
import com.example.hms.hotel_management_system.dto.response.RoomResponseDTO;
import com.example.hms.hotel_management_system.enums.RoomType;
import com.example.hms.hotel_management_system.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class RoomControllerTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
    }

    @Test
    void testCreateRoom() throws Exception {
        RoomRequestDTO request = new RoomRequestDTO("101", RoomType.SINGLE, BigDecimal.valueOf(1000), true,1,"First room on first floor");
        RoomResponseDTO response = new RoomResponseDTO(UUID.randomUUID(),"101", RoomType.SINGLE, BigDecimal.valueOf(1000), true,1,"First room on first floor");

        Mockito.when(roomService.createRoom(any(RoomRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/room/add-room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Room created successfully"))
                .andExpect(jsonPath("$.data.roomNumber").value("101"));
    }

    @Test
    void testGetRoomByRoomNumber() throws Exception {
        RoomResponseDTO response = new RoomResponseDTO(UUID.randomUUID(),"101", RoomType.SINGLE, BigDecimal.valueOf(1000), true,1,"First room on first floor");

        Mockito.when(roomService.getRoomByRoomNumber("101")).thenReturn(response);

        mockMvc.perform(get("/room/get-by-room-number/101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Room fetched successfully"))
                .andExpect(jsonPath("$.data.roomNumber").value("101"));
    }

    @Test
    void testGetAvailableRooms() throws Exception {
        List<RoomResponseDTO> rooms = Arrays.asList(
                new RoomResponseDTO(UUID.randomUUID(),"101", RoomType.SINGLE, BigDecimal.valueOf(1000), true,1,"First room on first floor"),
                new RoomResponseDTO(UUID.randomUUID(),"101", RoomType.SINGLE, BigDecimal.valueOf(1000), true,1,"First room on first floor")
        );

        Mockito.when(roomService.getAvailableRooms(true)).thenReturn(rooms);

        mockMvc.perform(get("/room/get-by-available?isAvailable=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Rooms fetched successfully"))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void testGetAllRooms() throws Exception {
        List<RoomResponseDTO> rooms = Arrays.asList(
                new RoomResponseDTO(UUID.randomUUID(),"101", RoomType.SINGLE, BigDecimal.valueOf(1000), true,1,"First room on first floor"),
                new RoomResponseDTO(UUID.randomUUID(),"101", RoomType.SINGLE, BigDecimal.valueOf(1000), true,1,"First room on first floor")
        );

        Mockito.when(roomService.getAllRooms()).thenReturn(rooms);

        mockMvc.perform(get("/room/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("All rooms fetched successfully"))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void testUpdateRoomByRoomNumber() throws Exception {
        RoomRequestDTO request = new RoomRequestDTO("101", RoomType.SINGLE, BigDecimal.valueOf(1000), true,1,"First room on first floor");
        RoomResponseDTO response = new RoomResponseDTO(UUID.randomUUID(),"101", RoomType.SINGLE, BigDecimal.valueOf(2000), true,1,"First room on first floor");

        Mockito.when(roomService.updateRoomByRoomNumber(any(RoomRequestDTO.class), eq("101")))
                .thenReturn(response);

        mockMvc.perform(put("/room/update-room-by-room-number/101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Room updated successfully"))
                .andExpect(jsonPath("$.data.pricePerNight").value(2000));
    }
}
