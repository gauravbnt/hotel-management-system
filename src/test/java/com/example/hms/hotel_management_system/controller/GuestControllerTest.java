package com.example.hms.hotel_management_system.controller;

import com.example.hms.hotel_management_system.dto.request.GuestRequestDTO;
import com.example.hms.hotel_management_system.dto.response.GuestResponseDTO;
import com.example.hms.hotel_management_system.service.GuestService;
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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class GuestControllerTest {

    @Mock
    private GuestService guestService;

    @InjectMocks
    private GuestController guestController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(guestController).build();
    }


    @Test
    void testAddGuest() throws Exception {
        GuestRequestDTO request = new GuestRequestDTO("Gaurav","Patil", "gaurav@example.com", "9876543210","Pune");
        GuestResponseDTO response = new GuestResponseDTO(UUID.randomUUID(),"Gaurav","Patil", "gaurav@example.com", "9876543210","Pune",  Timestamp.valueOf(LocalDateTime.now()));

        Mockito.when(guestService.createGuest(any(GuestRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/guest/add-guest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Guest added successfully"))
                .andExpect(jsonPath("$.data.firstName").value("Gaurav"))
                .andExpect(jsonPath("$.data.email").value("gaurav@example.com"));
    }

    @Test
    void testGetAllGuests() throws Exception {
        List<GuestResponseDTO> guestList = Arrays.asList(
                new GuestResponseDTO(UUID.randomUUID(),"Gaurav","Patil", "gaurav@example.com", "9876543210","Pune",  Timestamp.valueOf(LocalDateTime.now())),
                new GuestResponseDTO(UUID.randomUUID(),"Ankit","Patil", "ankita@example.com", "9866543210","Pune",  Timestamp.valueOf(LocalDateTime.now()))
        );
        
        Mockito.when(guestService.getAllGuest()).thenReturn(guestList);

        mockMvc.perform(get("/guest/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Guests fetched successfully"))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void testGetGuestByEmail() throws Exception {
        GuestResponseDTO response =  new GuestResponseDTO(UUID.randomUUID(),"Gaurav","Patil", "gaurav@example.com", "9876543210","Pune",  Timestamp.valueOf(LocalDateTime.now()));


        Mockito.when(guestService.getGuestByEmail("gaurav@example.com")).thenReturn(response);

        mockMvc.perform(get("/guest/get-by-email/gaurav@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Guest fetched successfully"))
                .andExpect(jsonPath("$.data.email").value("gaurav@example.com"));
    }

    @Test
    void testUpdateGuestByEmail() throws Exception {
        GuestRequestDTO request = new GuestRequestDTO("Gaurav","Patil", "gaurav@example.com", "9876543210","Pune");
        GuestResponseDTO response = new GuestResponseDTO(UUID.randomUUID(),"Gaurav","Patil", "gaurav@example.com", "9876543210","Pune",  Timestamp.valueOf(LocalDateTime.now()));
        Mockito.when(guestService.updateGuestByEmail(any(GuestRequestDTO.class), eq("gaurav@example.com")))
                .thenReturn(response);

        mockMvc.perform(put("/guest/update-by-email/gaurav@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Guest updated successfully"))
                .andExpect(jsonPath("$.data.phoneNumber").value("9876543210"));
    }

    @Test
    void testDeleteGuestByEmail() throws Exception {
        Mockito.doNothing().when(guestService).deleteGuestByEmail("gaurav@example.com");

        mockMvc.perform(delete("/guest/delete-by-email/gaurav@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Guest deleted successfully"))
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}
