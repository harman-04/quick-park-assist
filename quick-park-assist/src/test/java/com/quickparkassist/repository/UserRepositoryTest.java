package com.quickparkassist.repository;

import com.quickparkassist.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class) // Use MockitoExtension for JUnit 5
 class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
     void setUp() {
        // Set up a mock User object
        user = new User("John Doe", "john.doe@example.com", "password123", "Available", "1234567890", "Some Address", "USER");
    }

    @Test
     void testFindByEmail() {
        // Mock the behavior of findByEmail
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        // Call the method and verify
        User result = userRepository.findByEmail(user.getEmail());

        // Verify that the result is the same as the mock user
        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());

        // Verify that the findByEmail method was called once
        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
     void testFindIdByEmail() {
        // Mock the behavior of findIdByEmail to return the user wrapped in an Optional
        when(userRepository.findIdByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Call the method and verify
        Optional<User> result = userRepository.findIdByEmail(user.getEmail());

        // Verify that the result is present and matches the mock user
        assertTrue(result.isPresent());
        assertEquals(user.getEmail(), result.get().getEmail());

        // Verify that the findIdByEmail method was called once
        verify(userRepository).findIdByEmail(user.getEmail());
    }

    @Test
     void testFindIdByEmail_NotFound() {
        // Mock the behavior of findIdByEmail to return empty when the email is not found
        when(userRepository.findIdByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Call the method and verify
        Optional<User> result = userRepository.findIdByEmail("nonexistent@example.com");

        // Verify that the result is empty
        assertFalse(result.isPresent());

        // Verify that the findIdByEmail method was called once
        verify(userRepository).findIdByEmail("nonexistent@example.com");
    }

    @Test
     void testFindById() {
        // Mock the behavior of findById to return the user wrapped in an Optional
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Call the method and verify
        Optional<User> result = userRepository.findById(user.getId());

        // Verify that the result is present and matches the mock user
        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());

        // Verify that the findById method was called once
        verify(userRepository).findById(user.getId());
    }

    @Test
     void testFindById_NotFound() {
        // Mock the behavior of findById to return empty when the user is not found
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Call the method and verify
        Optional<User> result = userRepository.findById(999L);

        // Verify that the result is empty
        assertFalse(result.isPresent());

        // Verify that the findById method was called once
        verify(userRepository).findById(999L);
    }
}
