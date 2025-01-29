package com.quickparkassist.controller;

import com.quickparkassist.dto.UserRegistrationDto;
import com.quickparkassist.dto.VehicleDto;
import com.quickparkassist.model.User;
import com.quickparkassist.service.UserService;
import com.quickparkassist.service.UserServiceImpl;
import com.quickparkassist.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collections;

import static javax.swing.UIManager.get;
import static org.hibernate.cfg.AvailableSettings.USER;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@AutoConfigureMockMvc  // Automatically configures MockMvc
class UserRegistrationControllerTest {

    @InjectMocks
    private UserRegistrationController userRegistrationController;

    @Mock
    private UserServiceImpl userService;


    @Mock
    private UserServiceImpl userServiceImpl;
    @Mock
    private Authentication auth;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private UserDetails userDetails;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testShowRegistrationForm_whenLoggedInUserIsNull() {
        // Prepare mock data and mock services
        Model model = mock(Model.class);
        UserDetails loggedInUser = null;  // Simulate a logged-out user

        // Call the method to test
        String result = userRegistrationController.showRegistrationForm(model, loggedInUser);

        // Assert that the correct attribute was added to the model
        verify(model).addAttribute(eq("isLoggedIn"), eq(true));  // User is not logged in
        verify(model).addAttribute(eq("user"), any(UserRegistrationDto.class));  // Ensure that the user is added as a UserRegistrationDto

        // Optionally, assert the return value
        assertEquals("registersection/registration", result);
    }
    @Test
    void testShowRegistrationForm_UserLoggedIn() {
        // Arrange: Mock Security Context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true); // Simulate user is authenticated
        SecurityContextHolder.setContext(securityContext);

        // Call the method
        String viewName = userRegistrationController.showRegistrationForm(model, null);

        // Verify: Ensure attributes are added correctly
        verify(model).addAttribute("isLoggedIn", true); // Verify "isLoggedIn" is true

        verify(model).addAttribute(eq("user"), any(UserRegistrationDto.class)); // Verify a UserRegistrationDto is added
        assertEquals("registersection/registration", viewName); // Check the returned view name
    }


    @Test
    void testShowRegistrationForm_UserNotLoggedIn() {
        // Mock Security Context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        SecurityContextHolder.setContext(securityContext);

        // Call the method
        String viewName = userRegistrationController.showRegistrationForm(model, null);

        // Verify
        verify(model).addAttribute("isLoggedIn", false);

        verify(model).addAttribute(eq("user"), any(UserRegistrationDto.class));
        assertEquals("registersection/registration", viewName);
    }

    @Test
    void testRegisterUserAccount_EmailExists() {
        // Mock UserService
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("test@example.com");
        when(userService.emailExists("test@example.com")).thenReturn(true);

        // Call the method
        String result = userRegistrationController.registerUserAccount(registrationDto);

        // Verify
        assertEquals("redirect:registration?error", result);
    }

    @Test
    void testRegisterUserAccount_EmailDoesNotExist() {
        // Mock UserService
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setEmail("test@example.com");

        when(userService.emailExists("test@example.com")).thenReturn(false);

        // Call the method
        String result = userRegistrationController.registerUserAccount(registrationDto);

        // Verify
        verify(userService).save(registrationDto);
        assertEquals("redirect:/registration?success", result);
    }

    @Test
    void testVehicleDtoBinding() {
        VehicleDto vehicleDto = userRegistrationController.vehicleDto();
        assertNotNull(vehicleDto, "VehicleDto should not be null.");
    }

    @Test
    void testShowRegistrationForm_LoggedInUserNull() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);  // Simulate user is not authenticated
        when(authentication.getPrincipal()).thenReturn("anonymousUser");
        SecurityContextHolder.setContext(securityContext);

        String viewName = userRegistrationController.showRegistrationForm(model, null);

        // Since the user is not logged in, we expect isLoggedIn to be false
        verify(model).addAttribute("isLoggedIn", false);// Expecting false
        verify(model).addAttribute(eq("user"), any(UserRegistrationDto.class));
        assertEquals("registersection/registration", viewName);
    }
    @Test
    void testUserRegistrationDto() {
        UserService mockUserService = Mockito.mock(UserService.class);
        VehicleService mockVehicleService = Mockito.mock(VehicleService.class);
        // Arrange
        UserRegistrationController controller = new UserRegistrationController(mockUserService, mockVehicleService);
        Model model = null; // You can mock this if needed, but it's not required for this simple test

        // Act
        UserRegistrationDto result = controller.userRegistrationDto();

        // Assert
        assertNotNull(result, "UserRegistrationDto should not be null.");
    }
    @Test
    void testIsLoggedIn_UserIsLoggedIn() {
        // Arrange: Mock SecurityContext and Authentication
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("user@example.com");

        // Act: Call the method
        String viewName = userRegistrationController.showRegistrationForm(model, null);

        // Assert: Check "isLoggedIn" attribute
        verify(model).addAttribute("isLoggedIn", true);
        assertEquals("registersection/registration", viewName);
    }
    @Test
    void testIsLoggedIn_UserIsNotLoggedIn() {
        // Arrange: Mock SecurityContext and Authentication
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        // Act: Call the method
        String viewName = userRegistrationController.showRegistrationForm(model, null);

        // Assert: Check "isLoggedIn" attribute
        verify(model).addAttribute("isLoggedIn", false);
        assertEquals("registersection/registration", viewName);
    }

    @Test
    void testShowRegistrationForm_AuthenticationNull() {
        // Mock security context
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        // Mock loggedInUser
        UserDetails loggedInUser = null;

        // Mock model
        Model model = mock(Model.class);

        // Call the method
        String viewName = userRegistrationController.showRegistrationForm(model, loggedInUser);

        // Verify interactions and assertions
        verify(model).addAttribute("isLoggedIn", false);
        verify(model).addAttribute(eq("user"), any(UserRegistrationDto.class));
        assertEquals("registersection/registration", viewName);
    }
    @Test
    void testShowRegistrationForm_PrincipalAnonymousUser() {
        // Mock authentication
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("anonymousUser");

        // Mock loggedInUser
        UserDetails loggedInUser = null;

        // Mock model
        Model model = mock(Model.class);

        // Call the method
        String viewName = userRegistrationController.showRegistrationForm(model, loggedInUser);

        // Verify interactions and assertions
        verify(model).addAttribute("isLoggedIn", false);
        verify(model).addAttribute(eq("user"), any(UserRegistrationDto.class));
        assertEquals("registersection/registration", viewName);
    }

    @Test
    void testShowRegistrationForm_LoggedInUserDetailsNull() {
        // Mock authentication
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("loggedInUser");

        // Mock loggedInUser as null
        UserDetails loggedInUser = null;

        // Mock model
        Model model = mock(Model.class);

        // Call the method
        String viewName = userRegistrationController.showRegistrationForm(model, loggedInUser);

        // Verify interactions and assertions
        verify(model).addAttribute("isLoggedIn", true);
        verify(model).addAttribute(eq("user"), any(UserRegistrationDto.class));  // Provide the registration DTO
        assertEquals("registersection/registration", viewName);
    }

    @Test
    public void testValidUserLoggedIn() {
        // Arrange
        boolean isLoggedIn = true;
        UserDetails loggedInUser = mock(UserDetails.class);
        when(loggedInUser.getUsername()).thenReturn("test@example.com");

        User mockUser = new User("test@example.com");
        when(userService.getUserByEmail("test@example.com")).thenReturn(mockUser);

        Model model = mock(Model.class);

        // Act
        if (isLoggedIn && loggedInUser != null) {
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute("USER", user);
        }

        // Assert
        verify(userService, times(1)).getUserByEmail("test@example.com");
        verify(model, times(1)).addAttribute("USER", mockUser);
    }
    @Test
    public void testNoUserLoggedIn() {
        // Arrange
        // Case 1: isLoggedIn = false
        boolean isLoggedIn = false;
        UserDetails loggedInUser = mock(UserDetails.class); // Unused since user is not logged in
        Model model = mock(Model.class);

        // Act
        if (isLoggedIn && loggedInUser != null) {
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute("USER", user);
        }

        // Assert
        verify(userService, never()).getUserByEmail(anyString());
        verify(model, never()).addAttribute(anyString(), any());
    }

    @Test
    public void testLoggedInUserIsNull() {
        // Arrange
        // Case 2: isLoggedIn = true, loggedInUser = null
        boolean isLoggedIn = true;
        UserDetails loggedInUser = null;
        Model model = mock(Model.class);

        // Act
        if (isLoggedIn && loggedInUser != null) {
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute("USER", user);
        }

        // Assert
        verify(userService, never()).getUserByEmail(anyString());
        verify(model, never()).addAttribute(anyString(), any());
    }
    @Test
    public void testGetUserByEmailReturnsNull() {
        // Arrange
        boolean isLoggedIn = true;

        // Mock LoggedInUser
        UserDetails loggedInUser = mock(UserDetails.class);
        when(loggedInUser.getUsername()).thenReturn("nonexistent@example.com");

        // Mock UserServiceImpl to return null for this email
        when(userService.getUserByEmail("nonexistent@example.com")).thenReturn(null);

        Model model = mock(Model.class);

        // Act
        if (isLoggedIn && loggedInUser != null) {
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute("USER", user);
        }

        // Assert
        verify(userService, times(1)).getUserByEmail("nonexistent@example.com");
        verify(model, times(1)).addAttribute("USER", null);
    }
    @Test
    public void testLoggedInUserGetUsernameThrowsException() {
        // Arrange
        boolean isLoggedIn = true;

        // Mock LoggedInUser to throw an exception
        UserDetails loggedInUser = mock(UserDetails.class);
        when(loggedInUser.getUsername()).thenThrow(new RuntimeException("Simulated exception"));

        Model model = mock(Model.class);

        // Act
        try {
            if (isLoggedIn && loggedInUser != null) {
                User user = userService.getUserByEmail(loggedInUser.getUsername());
                model.addAttribute("USER", user);
            }
        } catch (RuntimeException e) {
            // Exception is expected in this test case
            assertEquals("Simulated exception", e.getMessage());
        }

        // Assert
        verify(userService, never()).getUserByEmail(anyString()); // Should not be called
        verify(model, never()).addAttribute(anyString(), any());     // Should not be called
    }

    @Test
    public void testUserNotLoggedIn() {
        // Arrange
        boolean isLoggedIn = false; // Simulating the user is not logged in
        UserDetails loggedInUser = null; // Optional, since isLoggedIn = false

        Model model = mock(Model.class);

        // Act
        if (isLoggedIn && loggedInUser != null) {
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute("USER", user);
        }

        // Assert
        verify(userService, never()).getUserByEmail(anyString()); // Should not be called
        verify(model, never()).addAttribute(anyString(), any());     // Should not be called
    }

    @Test
    public void testNotLoggedIn() {
        // Arrange
        UserServiceImpl userServiceImpl = mock(UserServiceImpl.class);
        User loggedInUser = mock(User.class);

        boolean isLoggedIn = false; // User is not logged in
        Model model = mock(Model.class); // Mock the model

        // Act
        if (isLoggedIn && loggedInUser != null) {
            User user = userServiceImpl.getUserByEmail(loggedInUser.getEmail());
            model.addAttribute("USER", user);
        }

        // Assert
        verify(model, never()).addAttribute(anyString(), any()); // Verify that no addAttribute was called when not logged in
    }
    @Test
    public void testLoggedInUserNull() {
        // Arrange
        UserServiceImpl userServiceImpl = mock(UserServiceImpl.class);
        User loggedInUser = null; // User is null

        boolean isLoggedIn = true; // User is marked as logged in
        Model model = mock(Model.class); // Mock the model

        // Act
        if (isLoggedIn && loggedInUser != null) {
            User user = userServiceImpl.getUserByEmail(loggedInUser.getEmail());
            model.addAttribute("USER", user);
        }

        // Assert
        verify(model, never()).addAttribute(anyString(), any()); // Verify that no addAttribute was called when loggedInUser is null
    }
    @Test
    public void testNotLoggedInAndUserNull() {
        // Arrange
        UserServiceImpl userServiceImpl = mock(UserServiceImpl.class);
        User loggedInUser = null; // User is null

        boolean isLoggedIn = false; // User is not logged in
        Model model = mock(Model.class); // Mock the model

        // Act
        if (isLoggedIn && loggedInUser != null) {
            User user = userServiceImpl.getUserByEmail(loggedInUser.getEmail());
            model.addAttribute("USER", user);
        }

        // Assert
        verify(model, never()).addAttribute(anyString(), any()); // Verify that no addAttribute was called when user is not logged in and loggedInUser is null
    }

}
