package com.example.hotelmanagementsystem.controller.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.hotelmanagementsystem.dto.request.RoomRequestDTO;
import com.example.hotelmanagementsystem.enums.RoomType;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RoomControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

     private RoomRequestDTO roomRequestDTO;

    @BeforeEach
    void setUp()throws Exception{
        roomRequestDTO= new RoomRequestDTO("102",RoomType.SINGLE,BigDecimal.valueOf(1000),true,1,"first floor second room");
            mockMvc.perform(post("/room/add-room")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomRequestDTO)))
                .andExpect(status().isCreated());
    
    }

    @Test
    void testGetRoomByRoomNumber()throws Exception{
        mockMvc.perform(get("/room/get-by-room-number/{roomNumber}", "102"))
                                .andExpect(status().isOk());
    }    

    @Test
    void testGetRoomByAvailable() throws Exception {
            mockMvc.perform(get("/room/get-by-available")
            .param("isAvailable","true"))
                            .andExpect(status().isOk());
        }

    @Test
    void testGetAll() throws Exception {
                mockMvc.perform(get("/room/get-all"))
                                .andExpect(status().isOk());
    }

    @Test
        void testUpdateRoomByRoomNumber() throws Exception {

            String desc="Nice room";
            roomRequestDTO.setDescription(desc);

                mockMvc.perform(put("/room/update-room-by-room-number/{roomNumber}", "102")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(roomRequestDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.description").value(desc));
        }

        
}
