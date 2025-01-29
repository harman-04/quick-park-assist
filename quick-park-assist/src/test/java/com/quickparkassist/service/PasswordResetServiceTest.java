package com.quickparkassist.service;

import com.quickparkassist.model.PasswordResetToken;
import com.quickparkassist.model.User;
import com.quickparkassist.repository.TokenRepository;
import com.quickparkassist.repository.UserRepository;
import com.quickparkassist.service.PasswordResetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PasswordResetServiceTest {

    @InjectMocks
    private PasswordResetService passwordResetService; // Service being tested

    @Mock
    private UserRepository userRepository; // Mocked User repository

    @Mock
    private TokenRepository tokenRepository; // Mocked Token repository

    @Mock
    private JavaMailSender javaMailSender; // Mocked mail sender

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this); // Initialize mocks
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setFullName("Test User");
        // Set other properties as needed
    }

    @Test
    void testSendEmail_Success() {
        String result = passwordResetService.sendEmail(testUser);

        assertEquals("success", result); // Verify that the method returns "success"
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class)); // Verify that an email was sent
    }

    @Test
    void testSendEmail_Error() {
        doThrow(new RuntimeException("Mail sending failed")).when(javaMailSender).send(any(SimpleMailMessage.class));

        String result = passwordResetService.sendEmail(testUser);

        assertEquals("error", result); // Verify that the method returns "error"
    }

    @Test
    void testDeleteExistingToken() {
        passwordResetService.deleteExistingToken(testUser);

        verify(tokenRepository, times(1)).deleteByUser(testUser); // Verify that deleteByUser was called once
    }

    @Test
    void testGenerateResetToken() {
        when(tokenRepository.save(any(PasswordResetToken.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Simulate saving the token

        String resetLink = passwordResetService.generateResetToken(testUser);

        assertNotNull(resetLink); // Ensure a reset link is generated
        assertTrue(resetLink.contains("http://localhost:8082/resetPassword/")); // Check if the URL is correct

        // Verify that a new token was created and saved
        verify(tokenRepository, times(1)).deleteByUser(testUser); // Ensure existing tokens are deleted
        verify(tokenRepository, times(1)).save(any(PasswordResetToken.class)); // Ensure a new token is saved
    }

    @Test
    void testHasExpired() {
        LocalDateTime futureDateTime = LocalDateTime.now().plusMinutes(10);
        LocalDateTime pastDateTime = LocalDateTime.now().minusMinutes(10);

        assertFalse(passwordResetService.hasExipred(futureDateTime)); // Should return false for future date
        assertTrue(passwordResetService.hasExipred(pastDateTime)); // Should return true for past date
    }
}
