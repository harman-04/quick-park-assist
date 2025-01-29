package com.quickparkassist.util;


import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserContextTest {
    @Test
    void testUserContextConstructor() throws Exception {
        Constructor<UserContext> constructor = UserContext.class.getDeclaredConstructor();
        constructor.setAccessible(true); // Make the constructor accessible

        // This line should throw the expected UnsupportedOperationException
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> constructor.newInstance());

        // Check that the cause of the exception is the UnsupportedOperationException
        assertEquals(UnsupportedOperationException.class, exception.getCause().getClass());

        constructor.setAccessible(false); // Cleanup
    }

//    @Test
//    void testPrivateConstructor() throws Exception {
//        // Get the private constructor of UserContext class
//        Constructor<UserContext> constructor = UserContext.class.getDeclaredConstructor();
//
//        // Ensure the constructor is private by default
//        assertFalse(constructor.isAccessible(), "Constructor should be private by default");
//
//        // Set the constructor to be accessible
//        constructor.setAccessible(true);
//
//        // Try to invoke the private constructor, which should throw an UnsupportedOperationException
//        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
//            constructor.newInstance();
//        });
//
//        // Verify that the cause of the InvocationTargetException is UnsupportedOperationException
//        assertEquals(UnsupportedOperationException.class, exception.getCause().getClass(),
//                "Expected UnsupportedOperationException to be thrown");
//
//        // Reset the constructor accessibility
//        constructor.setAccessible(false);
//    }
    @Test
    void testGetCurrentUsernameWhenAuthenticated() {
        // Mock SecurityContext and Authentication
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);

        // Set up mock behavior
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getName()).thenReturn("testUser");
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);

        // Set the SecurityContextHolder with the mocked SecurityContext
        SecurityContextHolder.setContext(mockSecurityContext);

        // Call the method and verify the result
        String username = UserContext.getCurrentUsername();
        assertEquals("testUser", username);

        // Clean up
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetCurrentUsernameWhenNotAuthenticated() {
        // Mock SecurityContext and Authentication
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);

        // Set up mock behavior
        when(mockAuthentication.isAuthenticated()).thenReturn(false);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);

        // Set the SecurityContextHolder with the mocked SecurityContext
        SecurityContextHolder.setContext(mockSecurityContext);

        // Call the method and verify the result
        String username = UserContext.getCurrentUsername();
        assertNull(username);

        // Clean up
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetCurrentUsernameWhenNoAuthentication() {
        // Mock SecurityContext
        SecurityContext mockSecurityContext = mock(SecurityContext.class);

        // Set up mock behavior
        when(mockSecurityContext.getAuthentication()).thenReturn(null);

        // Set the SecurityContextHolder with the mocked SecurityContext
        SecurityContextHolder.setContext(mockSecurityContext);

        // Call the method and verify the result
        String username = UserContext.getCurrentUsername();
        assertNull(username);

        // Clean up
        SecurityContextHolder.clearContext();
    }
}
