package com.quickparkassist.service;

import com.quickparkassist.dto.VehicleDto;
import com.quickparkassist.model.User;
import com.quickparkassist.model.Vehicle;
import com.quickparkassist.repository.UserRepository;
import com.quickparkassist.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VehicleServiceTest {

    @InjectMocks
    private VehicleService vehicleService; // The service being tested

    @Mock
    private VehicleRepository vehicleRepository; // Mocked Vehicle repository

    @Mock
    private UserRepository userRepository; // Mocked User repository

    @Mock
    private Authentication authentication; // Mocked Authentication

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this); // Initialize mocks

        // Set up a test user
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setFullName("Test User");

        // Set the authentication context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn(testUser.getEmail());
    }

    @Test
    void testSaveVehicle_Success() {
        // Arrange
        VehicleDto vehicleDto = new VehicleDto();
        vehicleDto.setVehicleNumber("ABC123");
        vehicleDto.setVehicleModel("Toyota");
        vehicleDto.setHasElectric("no");

        when(userRepository.findIdByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        // Act
        vehicleService.save(vehicleDto);

        // Assert
        verify(vehicleRepository, times(1)).save(any(Vehicle.class)); // Verify that save was called on the repository
    }

    @Test
    void testSaveVehicle_UserNotFound() {
        // Arrange
        VehicleDto vehicleDto = new VehicleDto();
        vehicleDto.setVehicleNumber("ABC123");

        when(userRepository.findIdByEmail(testUser.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            vehicleService.save(vehicleDto);
        });

        assertEquals("User not found", exception.getMessage()); // Verify that the correct exception is thrown
    }

    @Test
    void testDeleteVehicleById_Success() {
        // Arrange
        Long vehicleId = 1L;
        when(vehicleRepository.existsById(vehicleId)).thenReturn(true);

        // Act
        vehicleService.deleteVehicleById(vehicleId);

        // Assert
        verify(vehicleRepository, times(1)).deleteById(vehicleId); // Verify that deleteById was called on the repository
    }

    @Test
    void testDeleteVehicleById_NotFound() {
        // Arrange
        Long vehicleId = 1L;
        when(vehicleRepository.existsById(vehicleId)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            vehicleService.deleteVehicleById(vehicleId);
        });

        assertEquals("Vehicle not found with ID: " + vehicleId, exception.getMessage()); // Verify that the correct exception is thrown
    }
}
