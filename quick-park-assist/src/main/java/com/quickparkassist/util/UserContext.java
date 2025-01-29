package com.quickparkassist.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserContext {

    private UserContext() {
        // Prevent instantiation
        throw new UnsupportedOperationException("UserContext is a utility class and cannot be instantiated.");

    }

    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }
}
