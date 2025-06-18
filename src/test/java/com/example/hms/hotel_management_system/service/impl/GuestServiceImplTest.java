package com.example.hms.hotel_management_system.service.impl;

import com.example.hms.hotel_management_system.dto.request.GuestRequestDTO;
import com.example.hms.hotel_management_system.dto.response.GuestResponseDTO;
import com.example.hms.hotel_management_system.entity.Guest;
import com.example.hms.hotel_management_system.exception.GuestAlreadyExistsException;
import com.example.hms.hotel_management_system.exception.GuestNotFoundException;
import com.example.hms.hotel_management_system.mapper.GuestMapper;
import com.example.hms.hotel_management_system.repository.GuestRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GuestServiceImplTest {

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private GuestMapper guestMapper;

    @InjectMocks
    private GuestServiceImpl guestService;

    private Guest guest;
    private GuestRequestDTO guestRequestDTO;
    private GuestResponseDTO guestResponseDTO;

    @BeforeEach
    void setUp() {
        guest = new Guest();
        guest.setEmail("test@example.com");
        guest.setFirstName("John");

        guestRequestDTO = new GuestRequestDTO();
        guestRequestDTO.setEmail("test@example.com");
        guestRequestDTO.setFirstName("John");

        guestResponseDTO = new GuestResponseDTO();
        guestResponseDTO.setEmail("test@example.com");
        guestResponseDTO.setFirstName("John");
    }

    @Test
    void testCreateGuest_Success() {
        when(guestRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(guestMapper.toEntity(guestRequestDTO)).thenReturn(guest);
        when(guestRepository.save(guest)).thenReturn(guest);
        when(guestMapper.toResponseDTO(guest)).thenReturn(guestResponseDTO);

        GuestResponseDTO response = guestService.createGuest(guestRequestDTO);

        assertEquals("test@example.com", response.getEmail());
        verify(guestRepository).save(guest);
    }

    @Test
    void testCreateGuest_AlreadyExists() {
        when(guestRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(GuestAlreadyExistsException.class, () -> guestService.createGuest(guestRequestDTO));
        verify(guestRepository, never()).save(any());
    }

    @Test
    void testGetAllGuest_Success() {
        when(guestRepository.findAll()).thenReturn(List.of(guest));
        when(guestMapper.toResponseDTOList(List.of(guest))).thenReturn(List.of(guestResponseDTO));

        List<GuestResponseDTO> result = guestService.getAllGuest();
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllGuest_NotFound() {
        when(guestRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(GuestNotFoundException.class, () -> guestService.getAllGuest());
    }

    @Test
    void testGetGuestByEmail_Success() {
        when(guestRepository.findByEmail("test@example.com")).thenReturn(guest);
        when(guestMapper.toResponseDTO(guest)).thenReturn(guestResponseDTO);

        GuestResponseDTO result = guestService.getGuestByEmail("test@example.com");
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void testGetGuestByEmail_NotFound() {
        when(guestRepository.findByEmail("test@example.com")).thenReturn(null);

        assertThrows(GuestNotFoundException.class, () -> guestService.getGuestByEmail("test@example.com"));
    }

    @Test
    void testUpdateGuestByEmail_Success() {
        Guest updatedGuest = new Guest();
        updatedGuest.setEmail("updated@example.com");

        when(guestMapper.toEntity(guestRequestDTO)).thenReturn(updatedGuest);
        when(guestRepository.findByEmail("test@example.com")).thenReturn(guest);
        when(guestRepository.save(any(Guest.class))).thenReturn(updatedGuest);
        when(guestMapper.toResponseDTO(any(Guest.class))).thenReturn(guestResponseDTO);

        GuestResponseDTO result = guestService.updateGuestByEmail(guestRequestDTO, "test@example.com");
        assertNotNull(result);
    }

    @Test
    void testUpdateGuestByEmail_NotFound() {
        when(guestRepository.findByEmail("test@example.com")).thenReturn(null);

        assertThrows(GuestNotFoundException.class, () -> guestService.updateGuestByEmail(guestRequestDTO, "test@example.com"));
    }

    @Test
    void testDeleteGuestByEmail_Success() {
        when(guestRepository.existsByEmail("test@example.com")).thenReturn(true);
        when(guestRepository.findByEmail("test@example.com")).thenReturn(guest);

        assertDoesNotThrow(() -> guestService.deleteGuestByEmail("test@example.com"));
        verify(guestRepository).delete(guest);
    }

    @Test
    void testDeleteGuestByEmail_NotFound() {
        when(guestRepository.existsByEmail("test@example.com")).thenReturn(false);

        assertThrows(GuestNotFoundException.class, () -> guestService.deleteGuestByEmail("test@example.com"));
    }
}
