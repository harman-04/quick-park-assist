package com.quickparkassist.controller;

import com.quickparkassist.dto.UserRegistrationDto;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ForgotPasswordControllerTest {

    @InjectMocks
    private ForgotPasswordController forgotPasswordController;

    @Mock
    private PasswordResetService passwordResetService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testForgotPassword() {
        String viewName = forgotPasswordController.forgotPassword();
        assertEquals("forgotPassword", viewName);
    }

    @Test
    void testForgotPasswordProcess_UserExists() {
        UserRegistrationDto userDTO = new UserRegistrationDto();
        userDTO.setEmail("test@example.com");

        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(passwordResetService.sendEmail(user)).thenReturn("success");

        String result = forgotPasswordController.forgotPassordProcess(userDTO);

        assertEquals("redirect:/forgotPassword?success", result);
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(passwordResetService, times(1)).sendEmail(user);
    }

    @Test
    void testForgotPasswordProcess_UserNotExists() {
        UserRegistrationDto userDTO = new UserRegistrationDto();
        userDTO.setEmail("nonexistent@example.com");

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        String result = forgotPasswordController.forgotPassordProcess(userDTO);

        assertEquals("redirect:/login?error", result);
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
        verifyNoInteractions(passwordResetService);
    }

    @Test
    void testResetPasswordForm_ValidToken() {
        PasswordResetToken token = new PasswordResetToken();
        User user = new User();
        user.setEmail("test@example.com");
        token.setUser(user);
        token.setExpiryDateTime(LocalDateTime.now().plusMinutes(10)); // Valid expiry

        when(tokenRepository.findByToken("valid-token")).thenReturn(token);
        when(passwordResetService.hasExipred(any(LocalDateTime.class))).thenReturn(false);

        String result = forgotPasswordController.resetPasswordForm("valid-token", model);

        assertEquals("resetPassword", result);
        verify(model, times(1)).addAttribute("email", "test@example.com");
    }
    @Test
    void testResetPasswordForm_ValidToken_NotExpired() {
        // Arrange
        String validToken = "valid-token";
        PasswordResetToken resetToken = new PasswordResetToken();
        User user = new User();
        user.setEmail("test@example.com");
        resetToken.setUser(user);
        resetToken.setExpiryDateTime(LocalDateTime.now().plusHours(1)); // Token is not expired

        // Mock the repository and service
        when(tokenRepository.findByToken(validToken)).thenReturn(resetToken);
        when(passwordResetService.hasExipred(resetToken.getExpiryDateTime())).thenReturn(false);

        // Act
        String result = forgotPasswordController.resetPasswordForm(validToken, model);

        // Assert
        assertEquals("resetPassword", result); // Should return the "resetPassword" view
        verify(model).addAttribute("email", user.getEmail()); // Verify that email is added to model
    }
    @Test
    void testResetPasswordForm_ExpiredToken() {
        // Arrange
        String expiredToken = "expired-token";
        PasswordResetToken resetToken = new PasswordResetToken();
        User user = new User();
        user.setEmail("test@example.com");
        resetToken.setUser(user);
        resetToken.setExpiryDateTime(LocalDateTime.now().minusMinutes(10)); // Token has expired

        // Mock the repository and service
        when(tokenRepository.findByToken(expiredToken)).thenReturn(resetToken);
        when(passwordResetService.hasExipred(resetToken.getExpiryDateTime())).thenReturn(true);

        // Act
        String result = forgotPasswordController.resetPasswordForm(expiredToken, model);

        // Assert
        assertEquals("redirect:/forgotPassword?error", result); // Should redirect to an expired token error page
        verifyNoInteractions(model); // Model should not be updated
    }

    @Test
    void testResetPasswordForm_InvalidToken() {
        when(tokenRepository.findByToken("invalid-token")).thenReturn(null);

        String result = forgotPasswordController.resetPasswordForm("invalid-token", model);

        assertEquals("redirect:/forgotPassword?error", result);
        verifyNoInteractions(model);
    }

    @Test
    void testPasswordResetProcess_UserExists() {
        UserRegistrationDto userDTO = new UserRegistrationDto();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("newpassword");

        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        String result = forgotPasswordController.passwordResetProcess(userDTO);

        assertEquals("redirect:/login", result);
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode("newpassword");
    }

    @Test
    void testPasswordResetProcess_UserNotExists() {
        UserRegistrationDto userDTO = new UserRegistrationDto();
        userDTO.setEmail("nonexistent@example.com");
        userDTO.setPassword("newpassword");

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        String result = forgotPasswordController.passwordResetProcess(userDTO);

        assertEquals("redirect:/login", result);
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }
}
