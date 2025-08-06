package com.example.hotelmanagementsystem.controller.integration;

import com.example.hotelmanagementsystem.dto.request.GuestRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GuestControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        private GuestRequestDTO guestRequestDTO;

        @BeforeEach
        void setUp() throws Exception{
                guestRequestDTO = new GuestRequestDTO();
                guestRequestDTO.setEmail("testguest@email.com");
                guestRequestDTO.setFirstName("Test");
                guestRequestDTO.setLastName("Guest");
                guestRequestDTO.setPhoneNumber("1234567890");
                guestRequestDTO.setAddress("Pune");

                mockMvc.perform(post("/guest/add-guest")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(guestRequestDTO)))
                                .andExpect(status().isCreated());

        }

        @Test
        void testGetAllGuests() throws Exception {
                mockMvc.perform(get("/guest/get-all"))
                                .andExpect(status().isOk());
        }

        @Test
        void testGetGuestByEmail() throws Exception {
                mockMvc.perform(get("/guest/get-by-email/{email}", "testguest@email.com"))
                                .andExpect(status().isOk());
        }

        @Test
        void testUpdateGuestByEmail() throws Exception {
                String updated = "tom";
                guestRequestDTO.setFirstName(updated);
                mockMvc.perform(put("/guest/update-by-email/{email}", "testguest@email.com")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(guestRequestDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.firstName").value(updated));
        }

        @Test
        void testDeleteGuestByEmail() throws Exception {
                mockMvc.perform(delete("/guest/delete-by-email/{email}", "testguest@email.com"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").value("Guest deleted successfully"))
                                .andExpect(jsonPath("$.data").isEmpty());
        }
}*/
