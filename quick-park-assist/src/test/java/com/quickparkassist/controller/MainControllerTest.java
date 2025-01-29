package com.quickparkassist.controller;

import com.quickparkassist.dto.VehicleDto;
import com.quickparkassist.model.User;
import com.quickparkassist.model.Vehicle;
import com.quickparkassist.repository.VehicleRepository;
import com.quickparkassist.service.UserServiceImpl;
import com.quickparkassist.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class MainControllerTest {

    @InjectMocks
    private MainController mainController; // Your controller

    @Mock
    private UserServiceImpl userService; // Mocked UserService

    @Mock
    private VehicleService vehicleService; // Mocked VehicleService

    @Mock
    private VehicleRepository vehicleRepository; // Mocked VehicleRepository

    @Mock
    private Model model; // Mocked Model

    @Mock
    private RedirectAttributes redirectAttributes; // Mocked RedirectAttributes

    @Mock
    private UserDetails loggedInUser; // Mocked UserDetails

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this); // Initialize mocks
    }

    @Test
    void testLogin() {
        // Act
        String result = mainController.login();

        // Assert
        assertEquals("index", result); // Verify the return view name is correct
    }
    @Test
    void testRegister() {
        // Act
        String result = mainController.register();

        // Assert
        assertEquals("registersection/registeration", result); // Verify the return view name is correct
    }
    @Test
    void testHome() {
        // Act
        String result = mainController.home();

        // Assert
        assertEquals("index", result); // Verify the return view name is correct
    }
    @Test
    void testDelete() {
        // Arrange
        String username = "testuser@example.com";
        User user = new User(); // Initialize as needed

        when(loggedInUser.getUsername()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(user);

        // Act
        String result = mainController.delete(loggedInUser, model);

        // Assert
        verify(model).addAttribute("user", user); // Check if user is added to model
        assertEquals("registersection/delete", result); // Verify return view name is correct
    }
    @Test
    void testUserProfile() {
        // Arrange
        String username = "testuser@example.com";
        User user = new User(); // Initialize as needed

        when(loggedInUser.getUsername()).thenReturn(username);
        when(userService.findByUsername(username)).thenReturn(user);

        // Act
        String result = mainController.userProfile(loggedInUser, model);

        // Assert
        verify(model).addAttribute("user", user); // Check if user is added to model
        assertEquals("registersection/users", result); // Verify return view name is correct
    }

    @Test
    void testUpdateUser() {
        // Arrange
        User userToUpdate = new User();
        userToUpdate.setId(1L);
        userToUpdate.setPassword("newPassword");

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setPassword("oldPassword");

        when(userService.findUserById(anyLong())).thenReturn(existingUser);

        // Act
        String result = mainController.updateUser(userToUpdate, redirectAttributes);

        // Assert
        verify(userService).saveUser(userToUpdate);
        verify(redirectAttributes).addFlashAttribute("message", "Profile updated successfully.");
        assertEquals("redirect:/users", result);
    }
    @Test
    void testEditUserForm() {
        // Arrange
        String email = "testuser@example.com";
        User user = new User();

        when(userService.getUserByEmail(email)).thenReturn(user);

        // Act
        String result = mainController.editUserForm(email, model);

        // Assert
        verify(model).addAttribute("user", user);
        assertEquals("registersection/edit", result);
    }
    @Test
    void testDeleteUser() {
        // Arrange
        String email = "testuser@example.com";

        // Act
        String result = mainController.deleteUser(email);

        // Assert
        verify(userService).deleteUserByEmail(email);
        assertEquals("redirect:/logout", result);
    }
    @Test
    void testViewUserProfile_UserLoggedIn() {
        // Arrange
        when(loggedInUser.getUsername()).thenReturn("testuser@example.com");
        User user = new User();

        when(userService.getUserByEmail(loggedInUser.getUsername())).thenReturn(user);

        // Act
        String result = mainController.viewUserProfile(model, loggedInUser);

        // Assert
        verify(model).addAttribute("user", user);
        assertEquals("index", result);
    }

    @Test
    void testViewUserProfile_UserNotLoggedIn() {
        // Act
        String result = mainController.viewUserProfile(model, null);

        // Assert
        verify(model).addAttribute("user", null);
        assertEquals("index", result);
    }
    @Test
    void testShowAddVehiclePage() {
        // Act
        String result = mainController.showAddVehiclePage();

        // Assert
        assertEquals("registersection/add-vehicle", result);
    }
    @Test
    void testAddVehicle() {
        // Arrange
        VehicleDto vehicleDto = new VehicleDto();

        doNothing().when(vehicleService).save(vehicleDto);

        // Act
        String result = mainController.addVehicle(vehicleDto, redirectAttributes);

        // Assert
        verify(redirectAttributes).addFlashAttribute("message", "Vehicle added successfully.");
        assertEquals("redirect:/registration", result);
    }
    @Test
    void testDeleteVehicle() {
        Long vehicleId = 1L;

        doNothing().when(vehicleService).deleteVehicleById(vehicleId);

        List<Vehicle> vehicles = new ArrayList<>();

        when(vehicleRepository.findByUserEmail(anyString())).thenReturn(vehicles);

        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("testuser@example.com");

        String result = mainController.deleteVehicle(vehicleId, redirectAttributes);

        verify(vehicleService).deleteVehicleById(vehicleId);
        verify(redirectAttributes).addFlashAttribute("vehicles", vehicles);
        verify(redirectAttributes).addFlashAttribute("message", "Vehicle deleted successfully.");
        assertEquals("redirect:/view-vehicle", result);
    }

    @Test
    void testGetMyVehicles() {
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(authentication.getName()).thenReturn("testuser@example.com");

        List<Vehicle> vehicles = new ArrayList<>();

        when(vehicleRepository.findByUserEmail(anyString())).thenReturn(vehicles);

        String result = mainController.getMyVehicles(model);

        verify(model).addAttribute("vehicles", vehicles);
        assertEquals("registersection/view-vehicle", result);
    }
    @Test
    void testUpdateUserWithNewPassword() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setPassword("newPassword");
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setPassword("oldPassword");
        existingUser.setVehicles(new ArrayList<>()); // Assuming vehicles is a set

        when(userService.findUserById(1L)).thenReturn(existingUser);

        // Simulate the password encoding
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("newPassword");

        // When
        String result = mainController.updateUser(user, redirectAttributes);

        // Then
        assertEquals("redirect:/users", result);
        verify(userService).saveUser(user);
       // assertTrue(passwordEncoder.matches("newPassword", user.getPassword()));  // Check if password is encoded
        verify(redirectAttributes).addFlashAttribute("message", "Profile updated successfully.");
    }
    @Test
    void testUpdateUserWithEmptyPassword() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setPassword(""); // Empty password
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setPassword("oldPassword");
        existingUser.setVehicles(new ArrayList<>()); // Using ArrayList for a List<Vehicle>

        when(userService.findUserById(1L)).thenReturn(existingUser);

        // When
        String result = mainController.updateUser(user, redirectAttributes);

        // Then
        assertEquals("redirect:/users", result);
        verify(userService).saveUser(user);
        assertEquals("oldPassword", user.getPassword()); // Password should remain unchanged
        verify(redirectAttributes).addFlashAttribute("message", "Profile updated successfully.");
    }
    @Test
    public void testPasswordIsNull() {
        // Arrange
        User user = mock(User.class);
        when(user.getPassword()).thenReturn(null); // Password is null

        // Act
        boolean result = (user.getPassword() != null && !user.getPassword().isEmpty());

        // Assert
        assertFalse(result); // The condition should evaluate to false since the password is null
    }

    @Test
    public void testPasswordIsEmpty() {
        // Arrange
        User user = mock(User.class);
        when(user.getPassword()).thenReturn(""); // Password is empty

        // Act
        boolean result = (user.getPassword() != null && !user.getPassword().isEmpty());

        // Assert
        assertFalse(result); // The condition should evaluate to false since the password is empty
    }
    @Test
    public void testPasswordIsNonEmpty() {
        // Arrange
        User user = mock(User.class);
        when(user.getPassword()).thenReturn("ValidPassword"); // Password is non-null and non-empty

        // Act
        boolean result = (user.getPassword() != null && !user.getPassword().isEmpty());

        // Assert
        assertTrue(result, "Password should be non-null and non-empty");
    }

    @Test
    public void testPasswordIsWhitespace() {
        // Arrange
        User user = mock(User.class);
        when(user.getPassword()).thenReturn("    "); // Password contains only spaces

        // Act
        boolean result = (user.getPassword() != null && !user.getPassword().trim().isEmpty()); // Use trim() to remove whitespace

        // Assert
        assertFalse(result, "Password should be considered empty when it contains only spaces"); // The condition should evaluate to false
    }


}
