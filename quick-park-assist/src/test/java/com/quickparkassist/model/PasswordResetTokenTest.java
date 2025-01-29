package com.quickparkassist.model;

import com.quickparkassist.repository.TokenRepository;
import com.quickparkassist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest // This annotation will configure an in-memory database for testing
class PasswordResetTokenTest {

    private PasswordResetToken passwordResetToken;

    // Integration Test for saving and retrieving PasswordResetToken
    @Autowired
    private TokenRepository tokenRepository; // Assuming you have this repository

    @Autowired
    private UserRepository userRepository; // Assuming you have a User repository

    private User testUser;

    @BeforeEach
    void setUp() {
        passwordResetToken = new PasswordResetToken(); // Initialize a new instance before each test
    }

    @Test
    void testSetAndGetId() {
        int id = 1;
        passwordResetToken.setId(id);
        assertEquals(id, passwordResetToken.getId());
    }

    @Test
    void testSetAndGetToken() {
        String token = "sample-token";
        passwordResetToken.setToken(token);
        assertEquals(token, passwordResetToken.getToken());
    }

    @Test
    void testSetAndGetExpiryDateTime() {
        LocalDateTime expiryDateTime = LocalDateTime.now().plusHours(1);
        passwordResetToken.setExpiryDateTime(expiryDateTime);
        assertEquals(expiryDateTime, passwordResetToken.getExpiryDateTime());
    }

    @Test
    void testSetAndGetUser() {
        User user = new User(); // Assuming User is a valid entity class
        user.setId(1L); // Set some properties if needed
        passwordResetToken.setUser(user);
        assertEquals(user, passwordResetToken.getUser());
    }


}
