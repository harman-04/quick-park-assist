package com.quickparkassist.service;


import com.quickparkassist.dto.UserRegistrationDto;
import com.quickparkassist.model.User;
import com.quickparkassist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private User user;
    private UserRegistrationDto userRegistrationDto;

    @BeforeEach
     void setUp() {
        MockitoAnnotations.initMocks(this);
        user = new User("John Doe", "john.doe@example.com", "password123", "Available", "1234567890", "123 Main St", "ROLE_USER");
        userRegistrationDto = new UserRegistrationDto("John Doe", "john.doe@example.com", "password123", "Available", "1234567890", "123 Main St", "ROLE_USER");
    }

    @Test
    void testConstructor_initialization() {
       // Ensure that the userService instance is created with the userRepository injected
       assertNotNull(userService);  // Verify that the service object is not null

       // Prepare test data for the findUserById method
       Long userId = 2L;
       User mockUser = new User();  // Mock the User object
       mockUser.setId(userId);  // Set mock user's ID
       when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));  // Mock repository behavior

       // Call the method to test the repository interaction
       User foundUser = userService.findUserById(userId);  // Assuming the method exists in UserServiceImpl

       // Verify that the repository method was called once with the correct parameter
       verify(userRepository, times(1)).findById(userId);

       // Optionally assert that the returned user matches the expected one
       assertNotNull(foundUser);
       assertEquals(userId, foundUser.getId());
    }

    @Test
     void testLoadUserByUsername_UserFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        UserDetails loadedUser = userService.loadUserByUsername(user.getEmail());

        assertNotNull(loadedUser);
        assertEquals(user.getEmail(), loadedUser.getUsername());
        assertEquals(user.getPassword(), loadedUser.getPassword());
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
     void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(user.getEmail()));
    }



    @Test
     void testFindUserById() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User foundUser = userService.findUserById(user.getId());

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
     void testFindUserById_UserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findUserById(user.getId()));
    }

    @Test
     void testDeleteUserByEmail() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        userService.deleteUserByEmail(user.getEmail());

        verify(userRepository, times(1)).delete(user);
    }

    @Test
     void testDeleteUserByEmail_UserNotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);

        userService.deleteUserByEmail(user.getEmail());

        verify(userRepository, times(0)).delete(any(User.class));
    }

    @Test
     void testEmailExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        boolean emailExists = userService.emailExists(user.getEmail());

        assertTrue(emailExists);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
     void testEmailNotExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);

        boolean emailExists = userService.emailExists(user.getEmail());

        assertFalse(emailExists);
        verify(userRepository, times(1)).findByEmail(user.getEmail());
    }

    @Test
     void testFindMobileNumberByUserId() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        String mobileNumber = userService.findMobileNumberByUserId(user.getId());

        assertEquals(user.getPhoneNumber(), mobileNumber);
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
     void testFindMobileNumberByUserId_UserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findMobileNumberByUserId(user.getId()));
    }

    @Test
    void testSaveUser() {
       // Arrange
       UserRegistrationDto registrationDto = new UserRegistrationDto();
       registrationDto.setFullName("John Doe");
       registrationDto.setEmail("john.doe@example.com");
       registrationDto.setPassword("password123");
       registrationDto.setAvailability("Available");
       registrationDto.setPhoneNumber("1234567890");
       registrationDto.setAddress("123 Street, City");
       registrationDto.setRole("USER");

       // Mock the password encoder to return an encoded password
       when(passwordEncoder.encode("password123")).thenReturn("encodedPassword123");

       // Mock the repository's save method to return the user
       User savedUser = new User(
               "John Doe",
               "john.doe@example.com",
               "encodedPassword123",
               "Available",
               "1234567890",
               "123 Street, City",
               "USER"
       );
       when(userRepository.save(any(User.class))).thenReturn(savedUser);

       // Act
       User result = userService.save(registrationDto);

       // Assert
       assertNotNull(result);  // Ensure the result is not null
       assertEquals("John Doe", result.getFullName());
       assertEquals("john.doe@example.com", result.getEmail());
       assertEquals("encodedPassword123", result.getPassword());  // Check if password is encoded
       assertEquals("Available", result.getAvailability());
       assertEquals("1234567890", result.getPhoneNumber());
       assertEquals("123 Street, City", result.getAddress());
       assertEquals("USER", result.getRole());

       // Verify that the repository save method was called once
       verify(userRepository, times(1)).save(any(User.class));
    }
    @Test
    void testGetLoggedInUser() {
       // Arrange
       String username = "test@example.com";
       User mockUser = new User();
       mockUser.setEmail(username);

       when(userRepository.findByEmail(username)).thenReturn(mockUser);
       SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null));

       // Act
       User loggedInUser = userService.getLoggedInUser();

       // Assert
       assertNotNull(loggedInUser);
       assertEquals(username, loggedInUser.getEmail());
    }

    @Test
    void testFindByUsername() {
       // Arrange
       String username = "test@example.com";
       User mockUser = new User();
       mockUser.setEmail(username);

       when(userRepository.findByEmail(username)).thenReturn(mockUser);

       // Act
       User foundUser = userService.findByUsername(username);

       // Assert
       assertNotNull(foundUser);
       assertEquals(username, foundUser.getEmail());
    }
    @Test
    void testFindUserIdByUsername_UserFound() {
       // Arrange
       String email = "test@example.com";
       Long expectedUserId = 1L;
       User mockUser = new User();
       mockUser.setId(expectedUserId);
       mockUser.setEmail(email);

       when(userRepository.findIdByEmail(email)).thenReturn(Optional.of(mockUser));

       // Act
       Long userId = userService.findUserIdByUsername(email);

       // Assert
       assertNotNull(userId);
       assertEquals(expectedUserId, userId);
    }

    @Test
    void testFindUserIdByUsername_UserNotFound() {
       // Arrange
       String email = "nonexistent@example.com";

       when(userRepository.findIdByEmail(email)).thenReturn(Optional.empty());

       // Act & Assert
       assertThrows(UsernameNotFoundException.class, () -> {
          userService.findUserIdByUsername(email);
       });
    }
    @Test
    void testGetAllUsers() {
       // Arrange
       List<User> users = Arrays.asList(
               new User("user1@example.com"),
               new User("user2@example.com")
       );
       when(userRepository.findAll()).thenReturn(users);

       // Act
       List<User> allUsers = userService.getAllUsers();

       // Assert
       assertNotNull(allUsers);
       assertEquals(2, allUsers.size());
    }

    @Test
    void testGetAllUsers_EmptyList() {
       // Arrange
       when(userRepository.findAll()).thenReturn(Collections.emptyList());

       // Act
       List<User> allUsers = userService.getAllUsers();

       // Assert
       assertNotNull(allUsers);
       assertTrue(allUsers.isEmpty());
    }

    @Test
    void testGetUserById_Found() {
       // Arrange
       Long userId = 1L;
       User mockUser = new User();
       mockUser.setId(userId);
       when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

       // Act
       User user = userService.getUserById(userId);

       // Assert
       assertNotNull(user);
       assertEquals(userId, user.getId());
    }

    @Test
    void testGetUserById_NotFound() {
       // Arrange
       Long userId = 1L;
       when(userRepository.findById(userId)).thenReturn(Optional.empty());

       // Act
       User user = userService.getUserById(userId);

       // Assert
       assertNull(user);
    }
    @Test
    void testSaveUser_Update() {
       // Arrange
       User user = new User("user@example.com");
       user.setId(1L);

       when(userRepository.save(user)).thenReturn(user);

       // Act
       userService.saveUser(user);

       // Assert
       verify(userRepository, times(1)).save(user);  // Verify that save is called once
    }
@Test
void testGetUserByEmail_Found() {
   // Arrange
   String email = "user@example.com";
   User mockUser = new User();
   mockUser.setEmail(email);

   when(userRepository.findByEmail(email)).thenReturn(mockUser);

   // Act
   User user = userService.getUserByEmail(email);

   // Assert
   assertNotNull(user);
   assertEquals(email, user.getEmail());
}

    @Test
    void testGetUserByEmail_NotFound() {
       // Arrange
       String email = "nonexistent@example.com";
       when(userRepository.findByEmail(email)).thenReturn(null);

       // Act
       User user = userService.getUserByEmail(email);

       // Assert
       assertNull(user);
    }
    @Test
    void testUserServiceImplConstructor() {
       // Arrange
       UserRepository userRepository = Mockito.mock(UserRepository.class); // Create a mock UserRepository
       UserServiceImpl userService = new UserServiceImpl(userRepository); // Create an instance of UserServiceImpl

       // Act & Assert
       assertNotNull(userService, "UserServiceImpl should be instantiated.");
       assertNotNull(userRepository, "UserRepository should be injected.");
    }
 }
