package com.quickparkassist.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserRegistrationDtoTest {

    @Test
    void testFullName() {
        // Arrange
        UserRegistrationDto dto = new UserRegistrationDto();

        // Act
        dto.setFullName("John Doe");

        // Assert
        assertEquals("John Doe", dto.getFullName());
    }

    @Test
    void testPhoneNumber() {
        // Arrange
        UserRegistrationDto dto = new UserRegistrationDto();

        // Act
        dto.setPhoneNumber("123-456-7890");

        // Assert
        assertEquals("123-456-7890", dto.getPhoneNumber());
    }

    @Test
    void testAddress() {
        // Arrange
        UserRegistrationDto dto = new UserRegistrationDto();

        // Act
        dto.setAddress("123 Main St, Springfield");

        // Assert
        assertEquals("123 Main St, Springfield", dto.getAddress());
    }

    @Test
    void testAvailability() {
        // Arrange
        UserRegistrationDto dto = new UserRegistrationDto();

        // Act
        dto.setAvailability("Full-Time");

        // Assert
        assertEquals("Full-Time", dto.getAvailability());
    }

    @Test
    void testRole() {
        // Arrange
        UserRegistrationDto dto = new UserRegistrationDto();

        // Act
        dto.setRole("Admin");

        // Assert
        assertEquals("Admin", dto.getRole());
    }
}
