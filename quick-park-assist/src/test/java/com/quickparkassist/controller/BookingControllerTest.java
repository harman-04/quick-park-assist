package com.quickparkassist.controller;

import com.quickparkassist.model.Booking;
import com.quickparkassist.model.Spot;
import com.quickparkassist.model.User;
import com.quickparkassist.model.Vehicle;
import com.quickparkassist.repository.BookingRepository;
import com.quickparkassist.repository.SpotRepository;
import com.quickparkassist.repository.VehicleRepository;
import com.quickparkassist.service.BookingService;
import com.quickparkassist.service.SpotService;
import com.quickparkassist.service.UserServiceImpl;
import com.quickparkassist.util.UserContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.*;
        import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class BookingControllerTest {

    @Mock
    private SpotRepository spotRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private SpotService spotService;

    @Mock
    private BookingService bookingService;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private BookingController bookingController;
    @MockBean
    private UserContext userContext;


    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Autowired
    private MockMvc mockMvc;

    //    UserDetails loggedInUser = (UserDetails) SecurityContextHolder
//            .getContext()
//            .getAuthentication()
//            .getPrincipal();
    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("testuser@example.com", null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    @AfterEach
    void tearDown() {
        // Clear the security context after each test
        SecurityContextHolder.clearContext();
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testMainBookingPage() {
        // Mock user and vehicles
        // User mockUser = new User(1L, "testuser", "testuser@example.com", "password", "USER");
        User mockUser = new User(
                "Test User",
                "testuser@example.com",
                "password",
                "Available",  // Assuming availability as a string
                "123-456-7890",  // Phone number
                "123 Main St",  // Address
                "USER"  // Role
        );
        Vehicle vehicle1 = new Vehicle(1L, "Car1", "ABC123");
        Vehicle vehicle2 = new Vehicle(2L, "Car2", "XYZ456");
        List<Vehicle> nonElectricVehicles = Arrays.asList(vehicle1, vehicle2);

        // Mock userService and vehicleRepository behavior
        when(userService.getUserByEmail("testuser")).thenReturn(mockUser);
        when(vehicleRepository.findByUserAndHasElectric(mockUser, "NO")).thenReturn(nonElectricVehicles);

        // Mock parking spots
        List<Spot> spots = new ArrayList<>();
        spots.add(new Spot(1L, "YES", "Downtown", 101, 20.00, "A1", "Downtown Parking Spot", "Active", "Station 1", "Regular"));
        when(spotRepository.findByAvailability("YES")).thenReturn(spots);

        // Mock Principal to simulate logged-in user
        // Principal mockPrincipal = () -> "testuser";
        // Mock UserDetails
        UserDetails mockUserDetails = new org.springframework.security.core.userdetails.User(
                "testuser",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        // Call the controller method with the mocked Principal
        String viewName = bookingController.mainBookingPage(model, mockUserDetails);

        // Verify model attributes
        verify(model).addAttribute("user", mockUser);
        verify(model).addAttribute("vehicles", nonElectricVehicles);
        verify(model).addAttribute("parkingSpots", spots);

        // Assert view name
        assertEquals("booking/booking", viewName);

        // Clear SecurityContext after test
        SecurityContextHolder.clearContext();
    }
    @Test
    @WithMockUser(username = "testuser@example.com", roles = {"USER"})
    void testBookingPageWithLoggedInUser() {
        // Mock UserDetails for the logged-in user
        UserDetails mockUserDetails = new org.springframework.security.core.userdetails.User(
                "testuser@example.com",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Mock User object
        // User mockUser = new User(1L, "testuser", "testuser@example.com", "password", "USER");
        User mockUser = new User(
                "Test User",
                "testuser@example.com",
                "password",
                "Available",  // Assuming availability as a string
                "123-456-7890",  // Phone number
                "123 Main St",  // Address
                "USER"  // Role
        );
        // Mock Vehicles
        Vehicle vehicle1 = new Vehicle(1L, "Car1", "ABC123");
        Vehicle vehicle2 = new Vehicle(2L, "Car2", "XYZ456");
        List<Vehicle> nonElectricVehicles = Arrays.asList(vehicle1, vehicle2);

        // Mock Parking Spots
        Spot spot1 = new Spot(1L, "YES", "Downtown", 101, 20.00, "A1", "Downtown Parking Spot", "Active", "Station 1", "Regular");
        List<Spot> availableSpots = Collections.singletonList(spot1);

        // Mock service/repository responses
        when(userService.getUserByEmail("testuser@example.com")).thenReturn(mockUser);
        when(vehicleRepository.findByUserAndHasElectric(mockUser, "NO")).thenReturn(nonElectricVehicles);
        when(spotRepository.findByAvailability("YES")).thenReturn(availableSpots);

        // Execute the controller method
        String viewName = bookingController.mainBookingPage(model, mockUserDetails);

        // Verify model attributes
        verify(model).addAttribute("user", mockUser);
        verify(model).addAttribute("vehicles", nonElectricVehicles);
        verify(model).addAttribute("parkingSpots", availableSpots);

        // Verify the returned view
        assertEquals("booking/booking", viewName);
    }
    @Test
    @WithMockUser(username = "testuser@example.com", roles = {"USER"})
    void testBookingPageWith_LoggedInUser() {
        // Mock UserDetails for the logged-in user
        UserDetails mockUserDetails = new org.springframework.security.core.userdetails.User(
                "testuser@example.com",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Mock User object
        // User mockUser = new User(1L, "testuser", "testuser@example.com", "password", "USER");
        User mockUser = new User(
                "Test User",
                "testuser@example.com",
                "password",
                "Available",  // Assuming availability as a string
                "123-456-7890",  // Phone number
                "123 Main St",  // Address
                "USER"  // Role
        );
        // Mock Vehicles
        Vehicle vehicle1 = new Vehicle(1L, "Car1", "ABC123");
        Vehicle vehicle2 = new Vehicle(2L, "Car2", "XYZ456");
        List<Vehicle> nonElectricVehicles = Arrays.asList(vehicle1, vehicle2);

        // Mock Parking Spots
        Spot spot1 = new Spot(1L, "YES", "Downtown", 101, 20.00, "A1", "Downtown Parking Spot", "Active", "Station 1", "Regular");
        List<Spot> availableSpots = Collections.singletonList(spot1);

        // Mock service/repository responses
        when(userService.getUserByEmail("testuser@example.com")).thenReturn(mockUser);
        when(vehicleRepository.findByUserAndHasElectric(mockUser, "NO")).thenReturn(nonElectricVehicles);
        when(spotRepository.findByAvailability("YES")).thenReturn(availableSpots);

        // Execute the controller method
        String viewName = bookingController.booking(model, mockUserDetails);

        // Verify model attributes
        verify(model).addAttribute("user", mockUser);
        verify(model).addAttribute("vehicles", nonElectricVehicles);
        verify(model).addAttribute("parkingSpots", availableSpots);

        // Verify the returned view
        assertEquals("booking/booking", viewName);
    }

    @Test
    void testBookingPageWithoutLoggedInUser() {
        // Execute the controller method with no logged-in user
        String viewName = bookingController.mainBookingPage(model, null);

        // Verify model attributes for guest user
        verify(model).addAttribute("user", null);
        verify(model).addAttribute("vehicles", null);
        verify(model).addAttribute("parkingSpots", Collections.emptyList());

        // Verify the returned view
        assertEquals("booking/booking", viewName);
    }
    @Test
    void testBookingPageWithout_LoggedInUser() {
        // Execute the controller method with no logged-in user
        String viewName = bookingController.booking(model, null);

        // Verify model attributes for guest user
        verify(model).addAttribute("user", null);
        verify(model).addAttribute("vehicles", null);
        verify(model).addAttribute("parkingSpots", Collections.emptyList());

        // Verify the returned view
        assertEquals("booking/booking", viewName);
    }
    @Test
    void testModifyBookingDetails() {
        Long mockUserId = 1L;

        // Mock userService to return user ID
        when(userService.findUserIdByUsername("testuser@example.com")).thenReturn(mockUserId);

        // Mock bookings
        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setUserId(mockUserId);
        booking1.setStatus(Booking.Status.CONFIRMED);
        booking1.setStartTime(LocalDateTime.now().plusDays(1));

        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setUserId(mockUserId);
        booking2.setStatus(Booking.Status.PENDING);
        booking2.setStartTime(LocalDateTime.now().plusDays(2));

        List<Booking> mockBookings = Arrays.asList(booking1, booking2);

        // Mock bookingService to return the mock bookings
        when(bookingService.findBookingsByUserIdAndStatuses(mockUserId, Arrays.asList(Booking.Status.CONFIRMED, Booking.Status.PENDING)))
                .thenReturn(mockBookings);

        // Call the method
        String viewName = bookingController.modifyBookingDetails(model);

        // Verify interactions
        verify(bookingService).updateBookingStatuses();
        verify(model).addAttribute("bookings", mockBookings);
        assertEquals("booking/modify-booking-details", viewName);
    }
    @Test
    void testModifyBookingDetails_NoBookings() {
        // Mock the logged-in user's email
        String mockEmail = "testuser@example.com";
        Long mockUserId = 1L;

        // Set up the SecurityContext with the mock user
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(mockEmail, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mock userService to return user ID
        when(userService.findUserIdByUsername(mockEmail)).thenReturn(mockUserId);

        // Mock bookingService to return an empty list
        when(bookingService.findBookingsByUserIdAndStatuses(eq(mockUserId), anyList()))
                .thenReturn(Collections.emptyList());

        // Call the method
        String viewName = bookingController.modifyBookingDetails(model);

        // Verify booking status update is called
        verify(bookingService).updateBookingStatuses();

        // Verify empty bookings list is added to the model
        verify(model).addAttribute("bookings", Collections.emptyList());

        // Verify the correct view is returned
        assertEquals("booking/modify-booking-details", viewName);

        // Clear the SecurityContext after the test
        SecurityContextHolder.clearContext();
    }
    // ✅ Test when booking exists
    @Test
    void testEditBooking_BookingExists() {
        Long bookingId = 1L;

        // Mock Booking
        Booking booking = new Booking();
        booking.setId(bookingId);

        // Mock Parking Spots
        Spot spot1 = new Spot();
        Spot spot2 = new Spot();
        List<Spot> mockSpots = Arrays.asList(spot1, spot2);

        // Mock Vehicles
        Vehicle vehicle1 = new Vehicle();
        Vehicle vehicle2 = new Vehicle();
        List<Vehicle> mockVehicles = Arrays.asList(vehicle1, vehicle2);

        // Mock service and repository responses
        when(bookingService.findById(bookingId)).thenReturn(Optional.of(booking));
        when(spotRepository.findAll()).thenReturn(mockSpots);
        when(vehicleRepository.findAll()).thenReturn(mockVehicles);

        // Call the method
        String viewName = bookingController.editBooking(bookingId, model);

        // Verify model attributes are set correctly
        verify(model).addAttribute("booking", booking);
        verify(model).addAttribute("parkingSpots", mockSpots);
        verify(model).addAttribute("vehicles", mockVehicles);

        // Verify the correct view is returned
        assertEquals("booking/edit-booking", viewName);
    }

    // ❌ Test when booking does NOT exist
    @Test
    void testEditBooking_BookingNotFound() {
        Long bookingId = 99L;

        // Mock service to return empty when booking not found
        when(bookingService.findById(bookingId)).thenReturn(Optional.empty());

        // Call the method
        String viewName = bookingController.editBooking(bookingId, model);

        // Verify error message is added to the model
        verify(model).addAttribute("message", "Booking not found");

        // Verify the correct fallback view is returned
        assertEquals("booking/view-booking", viewName);
    }

    // ✅ Test Case: Successful booking update
    @Test
    void testUpdateBooking_Success() {
        Long bookingId = 1L;
        Long spotId = 2L;
        Long vehicleId = 3L;

        // Mock Booking
        Booking booking = new Booking();
        booking.setId(bookingId);

        // Mock Spot
        Spot spot = new Spot();
        spot.setSpotId(spotId);
        spot.setLocation("A1");

        // Mock Vehicle
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        // Mock repository responses
        when(spotRepository.findById(spotId)).thenReturn(Optional.of(spot));
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        // Call the method
        String viewName = bookingController.updateBooking(
                bookingId,
                booking,
                String.valueOf(spotId),
                vehicleId,
                redirectAttributes,
                model
        );

        // Verify booking update
        verify(bookingService).saveBooking(booking);
        verify(redirectAttributes).addFlashAttribute("message", "Booking updated successfully");

        // Assert redirect to modify booking details
        assertEquals("redirect:/modify-Booking-Details", viewName);
    }

    // ❌ Test Case: Invalid Parking Spot
    @Test
    void testUpdateBooking_InvalidParkingSpot() {
        Long bookingId = 1L;
        Long invalidSpotId = 999L;
        Long vehicleId = 3L;

        Booking booking = new Booking();
        booking.setId(bookingId);

        // Mock spotRepository to return empty
        when(spotRepository.findById(invalidSpotId)).thenReturn(Optional.empty());

        String viewName = bookingController.updateBooking(
                bookingId,
                booking,
                String.valueOf(invalidSpotId),
                vehicleId,
                redirectAttributes,
                model
        );

        verify(model).addAttribute("message", "Parking spot not found.");
        assertEquals("booking/edit-booking", viewName);
    }

    // ❌ Test Case: Invalid Vehicle
    @Test
    void testUpdateBooking_InvalidVehicle() {
        Long bookingId = 1L;
        Long spotId = 2L;
        Long invalidVehicleId = 999L;

        Booking booking = new Booking();
        booking.setId(bookingId);

        Spot spot = new Spot();
        spot.setSpotId(spotId);

        // Mock valid spot but invalid vehicle
        when(spotRepository.findById(spotId)).thenReturn(Optional.of(spot));
        when(vehicleRepository.findById(invalidVehicleId)).thenReturn(Optional.empty());

        String viewName = bookingController.updateBooking(
                bookingId,
                booking,
                String.valueOf(spotId),
                invalidVehicleId,
                redirectAttributes,
                model
        );

        verify(model).addAttribute("message", "Vehicle not found.");
        assertEquals("booking/edit-booking", viewName);
    }

    // ❌ Test Case: Invalid Booking ID
    @Test
    void testUpdateBooking_InvalidBookingId() {
        Long pathBookingId = 1L;
        Long formBookingId = 2L;
        Long spotId = 3L;
        Long vehicleId = 4L;

        Booking booking = new Booking();
        booking.setId(formBookingId);

        Spot spot = new Spot();
        spot.setSpotId(spotId);

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);

        when(spotRepository.findById(spotId)).thenReturn(Optional.of(spot));
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        String viewName = bookingController.updateBooking(
                pathBookingId,
                booking,
                String.valueOf(spotId),
                vehicleId,
                redirectAttributes,
                model
        );

        verify(model).addAttribute("message", "Invalid booking ID.");
        assertEquals("booking/edit-booking", viewName);
    }

    // ❌ Test Case: Invalid Parking Spot Location Format
    @Test
    void testUpdateBooking_InvalidParkingSpotFormat() {
        Long bookingId = 1L;
        Long vehicleId = 2L;

        Booking booking = new Booking();
        booking.setId(bookingId);

        // Pass invalid parking spot location (non-numeric)
        String invalidSpotLocation = "invalid_spot";

        String viewName = bookingController.updateBooking(
                bookingId,
                booking,
                invalidSpotLocation,
                vehicleId,
                redirectAttributes,
                model
        );

        verify(model).addAttribute("message", "Invalid parking spot location.");
        assertEquals("booking/edit-booking", viewName);
    }

    @Test
    void testViewBookingPage() {
        // Simulate a direct call to the controller method
        String result = bookingController.viewBookingPage();

        // Assert the returned view name
        assertEquals("booking/view-booking-by-number", result);
    }
    // ✅ Test Case: Successful booking retrieval with valid mobile number
    @Test
    void testViewBooking_Success() {
        String mobileNumber = "9876543210";

        // Mock UserDetails for the logged-in user
        UserDetails mockUserDetails = new org.springframework.security.core.userdetails.User(
                "testuser@example.com",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Mock User object
        // User mockUser = new User(1L, "testuser", "testuser@example.com", "password", "USER");
        User mockUser = new User(
                "Test User",
                "testuser@example.com",
                "password",
                "Available",  // Assuming availability as a string
                "123-456-7890",  // Phone number
                "123 Main St",  // Address
                "USER"  // Role
        );
        Long userId = mockUser.getId();

        // Mock user service responses
        when(userService.findUserIdByUsername(mockUserDetails.getUsername())).thenReturn(userId);
        when(userService.findMobileNumberByUserId(userId)).thenReturn(mobileNumber);

        // Mock bookings
        Booking booking1 = new Booking();
        booking1.setStartTime(LocalDateTime.now().minusDays(1));

        Booking booking2 = new Booking();
        booking2.setStartTime(LocalDateTime.now().minusDays(2));

        List<Booking> bookings = Arrays.asList(booking1, booking2);

        when(bookingService.existsByMobileNumberAndUserId(mobileNumber, userId)).thenReturn(true);
        when(bookingService.findByMobileNumber(mobileNumber)).thenReturn(bookings);

        // Call the method
        String viewName = bookingController.viewBooking(mobileNumber, model);

        // Verify
        verify(bookingService).updateBookingStatuses();
        verify(model).addAttribute("bookings", bookings);
        verify(model).addAttribute("message", "Here are your booking details for the entered mobile number.");
        assertEquals("booking/view-booking-by-number", viewName);
    }

    // ❌ Test Case: Entered mobile number is invalid (doesn't match user's registered number)
    @Test
    void testViewBooking_InvalidMobileNumber() {
        String enteredMobileNumber = "1234567890";
        String registeredMobileNumber = "9876543210";

        // Mock UserDetails for the logged-in user
        UserDetails mockUserDetails = new org.springframework.security.core.userdetails.User(
                "testuser@example.com",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Mock User object
        //  User mockUser = new User(1L, "testuser", "testuser@example.com", "password", "USER");
        User mockUser = new User(
                "Test User",
                "testuser@example.com",
                "password",
                "Available",  // Assuming availability as a string
                "123-456-7890",  // Phone number
                "123 Main St",  // Address
                "USER"  // Role
        );
        Long userId = mockUser.getId();

        // Mock user service responses
        when(userService.findUserIdByUsername(mockUserDetails.getUsername())).thenReturn(userId);
        when(userService.findMobileNumberByUserId(userId)).thenReturn(registeredMobileNumber);

        when(bookingService.existsByMobileNumberAndUserId(enteredMobileNumber, userId)).thenReturn(false);

        // Call the method
        String viewName = bookingController.viewBooking(enteredMobileNumber, model);

        // Verify error handling
        verify(model).addAttribute("error", "Please enter your own registered mobile number.");
        assertEquals("redirect:/viewBookingByNumber", viewName);

      //  assertEquals("redirect:/viewBookingByNumber", viewName);
    }

    // ❌ Test Case: Entered mobile number belongs to another user
    @Test
    void testViewBooking_MobileNumberNotOwnedByUser() {
        String enteredMobileNumber = "1234567890";
        String registeredMobileNumber = "9876543210";

        // Mock UserDetails for the logged-in user
        UserDetails mockUserDetails = new org.springframework.security.core.userdetails.User(
                "testuser@example.com",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Mock User object
        // User mockUser = new User(1L, "testuser", "testuser@example.com", "password", "USER");
        User mockUser = new User(
                "Test User",
                "testuser@example.com",
                "password",
                "Available",  // Assuming availability as a string
                "123-456-7890",  // Phone number
                "123 Main St",  // Address
                "USER"  // Role
        );
        Long userId = mockUser.getId();

        // Mock user service responses
        when(userService.findUserIdByUsername(mockUserDetails.getUsername())).thenReturn(userId);
        when(userService.findMobileNumberByUserId(userId)).thenReturn(registeredMobileNumber);

        // Mobile number belongs to another user
        when(bookingService.existsByMobileNumberAndUserId(enteredMobileNumber, userId)).thenReturn(false);

        // Call the method
        String viewName = bookingController.viewBooking(enteredMobileNumber, model);

        // Verify error handling
        verify(model).addAttribute("error", "Please enter your own registered mobile number.");
        assertEquals("redirect:/viewBookingByNumber", viewName);

        // assertEquals("redirect:/viewBookingByNumber", viewName);
    }
    @Test
    void testViewBooking_ValidMobileNumber() {
        String mobileNumber = "9876543210";  // Valid registered mobile number
        String registeredMobileNumber = "9876543210";

        // Mock UserDetails for the logged-in user
        UserDetails mockUserDetails = new org.springframework.security.core.userdetails.User(
                "testuser@example.com",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Mock User object
        //  User mockUser = new User(1L, "testuser", "testuser@example.com", "password", "USER");
        User mockUser = new User(
                "Test User",
                "testuser@example.com",
                "password",
                "Available",  // Assuming availability as a string
                "123-456-7890",  // Phone number
                "123 Main St",  // Address
                "USER"  // Role
        );
        Long userId = mockUser.getId();

        // Mock user service responses
        when(userService.findUserIdByUsername(mockUserDetails.getUsername())).thenReturn(userId);
        when(userService.findMobileNumberByUserId(userId)).thenReturn(registeredMobileNumber);

//        // Mock booking service responses
//        List<Booking> mockBookings = new ArrayList<>();
//        mockBookings.add(new Booking(1L, mobileNumber, LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
//        mockBookings.add(new Booking(2L, mobileNumber, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1).plusHours(1)));
        List<Booking> mockBookings = new ArrayList<>();

        Booking booking1 = new Booking();
        booking1.setId(1L);
        booking1.setMobileNumber(mobileNumber);
        booking1.setStartTime(LocalDateTime.now());

        Booking booking2 = new Booking();
        booking2.setId(2L);
        booking2.setMobileNumber(mobileNumber);
        booking2.setStartTime(LocalDateTime.now().minusDays(1));

        mockBookings.add(booking1);
        mockBookings.add(booking2);

        when(bookingService.existsByMobileNumberAndUserId(mobileNumber, userId)).thenReturn(true);
        when(bookingService.findByMobileNumber(mobileNumber)).thenReturn(mockBookings);

        // Call the method
        String viewName = bookingController.viewBooking(mobileNumber, model);

        // Verify bookings are fetched and added to the model
        verify(model).addAttribute("bookings", mockBookings);
        verify(model).addAttribute("message", "Here are your booking details for the entered mobile number.");
        assertEquals("booking/view-booking-by-number", viewName);
    }

    // ✅ Test Case: Valid mobile number but no bookings found
    @Test
    void testViewBooking_NoBookingsFound() {
        String mobileNumber = "9876543210";

        // Mock UserDetails for the logged-in user
        UserDetails mockUserDetails = new org.springframework.security.core.userdetails.User(
                "testuser@example.com",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Mock User object
        //   User mockUser = new User(1L, "testuser", "testuser@example.com", "password", "USER");

        User mockUser = new User(
                "Test User",
                "testuser@example.com",
                "password",
                "Available",  // Assuming availability as a string
                "123-456-7890",  // Phone number
                "123 Main St",  // Address
                "USER"  // Role
        );
        Long userId = mockUser.getId();

        // Mock user service responses
        when(userService.findUserIdByUsername(mockUserDetails.getUsername())).thenReturn(userId);
        when(userService.findMobileNumberByUserId(userId)).thenReturn(mobileNumber);

        // No bookings found
        when(bookingService.existsByMobileNumberAndUserId(mobileNumber, userId)).thenReturn(true);
        when(bookingService.findByMobileNumber(mobileNumber)).thenReturn(Collections.emptyList());

        // Call the method
        String viewName = bookingController.viewBooking(mobileNumber, model);

        // Verify empty booking list
        verify(model).addAttribute("bookings", Collections.emptyList());
        verify(model).addAttribute("message", "Here are your booking details for the entered mobile number.");
        assertEquals("booking/view-booking-by-number", viewName);
    }
    @Test
    void testViewBooking_IsOwnerOfBookingTrue() {
        String enteredMobileNumber = "1234567890";
        String registeredMobileNumber = "9876543210"; // Different from entered number

        // Mock UserDetails for the logged-in user
        UserDetails mockUserDetails = new org.springframework.security.core.userdetails.User(
                "testuser@example.com",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Mock User object
        //  User mockUser = new User(1L, "testuser", "testuser@example.com", "password", "USER");

        User mockUser = new User(
                "Test User",
                "testuser@example.com",
                "password",
                "Available",  // Assuming availability as a string
                "123-456-7890",  // Phone number
                "123 Main St",  // Address
                "USER"  // Role
        );
        Long userId = mockUser.getId();

        // Mock user service responses
        when(userService.findUserIdByUsername(mockUserDetails.getUsername())).thenReturn(userId);
        when(userService.findMobileNumberByUserId(userId)).thenReturn(registeredMobileNumber);

        // Mock that the user owns the booking, even if the mobile number is different
        when(bookingService.existsByMobileNumberAndUserId(enteredMobileNumber, userId)).thenReturn(true);

        // Mock bookings for the entered mobile number
        List<Booking> mockBookings = new ArrayList<>();
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setMobileNumber(enteredMobileNumber);
        booking.setStartTime(LocalDateTime.now());
        mockBookings.add(booking);

        when(bookingService.findByMobileNumber(enteredMobileNumber)).thenReturn(mockBookings);

        // Call the method
        String viewName = bookingController.viewBooking(enteredMobileNumber, model);

        // Verify bookings are fetched and added to the model
        verify(model).addAttribute("bookings", mockBookings);
        verify(model).addAttribute("message", "Here are your booking details for the entered mobile number.");
        assertEquals("booking/view-booking-by-number", viewName);
    }

    //view Booking to cancel
    // ✅ Test Case: Fetch and display bookings with CONFIRMED and PENDING statuses
    @Test
    void testViewBookings_Success() {
        // Mock logged-in user's email
        String email = "testuser@example.com";
        Long userId = 1L;

        // Mock bookings with CONFIRMED and PENDING statuses
        Booking confirmedBooking = new Booking();
        confirmedBooking.setStatus(Booking.Status.CONFIRMED);
        confirmedBooking.setStartTime(LocalDateTime.now().plusDays(1));

        Booking pendingBooking = new Booking();
        pendingBooking.setStatus(Booking.Status.PENDING);
        pendingBooking.setStartTime(LocalDateTime.now().plusDays(2));

        List<Booking> mockBookings = Arrays.asList(confirmedBooking, pendingBooking);

        // Mock UserService and BookingService behavior
        when(userService.findUserIdByUsername(email)).thenReturn(userId);
        when(bookingService.findBookingsByUserIdAndStatuses(userId, Arrays.asList(Booking.Status.CONFIRMED, Booking.Status.PENDING)))
                .thenReturn(mockBookings);

        // Call the controller method
        String viewName = bookingController.viewBookings(model);

        // Verify model attribute and returned view
        verify(model).addAttribute("bookings", mockBookings);
        assertEquals("booking/cancel-booking", viewName);
    }

    // ❌ Test Case: No bookings found for CONFIRMED or PENDING statuses
    @Test
    void testViewBookings_NoBookingsFound() {
        String email = "testuser@example.com";
        Long userId = 1L;

        // Mock empty bookings list
        List<Booking> emptyBookings = Collections.emptyList();

        // Mock UserService and BookingService behavior
        when(userService.findUserIdByUsername(email)).thenReturn(userId);
        when(bookingService.findBookingsByUserIdAndStatuses(userId, Arrays.asList(Booking.Status.CONFIRMED, Booking.Status.PENDING)))
                .thenReturn(emptyBookings);

        // Call the controller method
        String viewName = bookingController.viewBookings(model);

        // Verify empty bookings list added to model
        verify(model).addAttribute("bookings", emptyBookings);
        assertEquals("booking/cancel-booking", viewName);
    }
    @Test
    void testMocking() {
        assertNotNull(spotService);
        // Additional test logic here
    }
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCancelBooking() {
        Long bookingId = 1L;

        // Create a Spot and Booking
        Spot spot = new Spot();
        spot.setSpotId(1L);
        spot.setAvailability("NO");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setSpot(spot);
        booking.setStatus(Booking.Status.CONFIRMED);

        // Mock behavior for bookingService.findById to return the booking
        when(bookingService.findById(bookingId)).thenReturn(Optional.of(booking));

        // Mock the booking status update in bookingService
        doAnswer(invocation -> {
            booking.setStatus(Booking.Status.CANCELLED);  // Simulate the update
            return null;
        }).when(bookingService).updateBookingStatusTOCancel(bookingId, Booking.Status.CANCELLED);
        // doNothing().when(bookingService).updateBookingStatusTOCancel(eq(bookingId), eq(Booking.Status.CANCELLED));

        // Call the controller method
        String result = bookingController.cancelBooking(bookingId, redirectAttributes);

        // Assertions to verify behavior
        assertEquals(Booking.Status.CANCELLED, booking.getStatus());  // Status should now be CANCELLED
        verify(bookingRepository).save(booking);                      // Ensure booking was saved

        // Assert spot availability is updated to "yes"
        assertEquals("yes", spot.getAvailability());
        verify(spotRepository).save(spot);                            // Ensure spot was saved

        // Verify redirection to the correct page
        assertEquals("redirect:/viewBookingToCancel", result);        // Verify redirection URL
        // ❌ Case 2: Booking Exists but Spot is Null - Should Throw Exception
        Booking bookingWithNullSpot = new Booking();
        bookingWithNullSpot.setId(bookingId);
        bookingWithNullSpot.setSpot(null);  // Spot is intentionally set to null
        bookingWithNullSpot.setStatus(Booking.Status.CONFIRMED);

        // Mock behavior for bookingService.findById to return the booking with null spot
        when(bookingService.findById(bookingId)).thenReturn(Optional.of(bookingWithNullSpot));

        // Assert that an IllegalStateException is thrown when the spot is null
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            bookingController.cancelBooking(bookingId, redirectAttributes);
        });

        // Verify the exception message
        assertEquals("No associated parking spot found for booking ID: " + bookingId, exception.getMessage());

        // Verify that booking and spot were NOT saved when spot is null
        verify(bookingRepository, times(1)).save(any());  // Only the valid booking was saved once
        verify(spotRepository, times(1)).save(any());     // Only the valid spot was saved once
    }
    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testBookSpot() {
        Long spotId = 1L;
        Long vehicleId = 101L;
        String username = "testuser";
        Long userId = 1001L;

        // Mock spot and vehicle data
        Spot spot = new Spot(spotId, "YES", "Downtown", 101, 20.00, "A1", "Downtown Parking Spot", "Active", "Station 1","Regular");
        Vehicle vehicle = new Vehicle(vehicleId, "Car Model", "XYZ123");

        // Mock repository and service calls
        lenient().when(userService.findUserIdByUsername(username)).thenReturn(userId);
        lenient().when(spotRepository.findById(spotId)).thenReturn(Optional.of(spot));
        lenient().when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        // Create a Booking object using the no-argument constructor and setters
        Booking booking = new Booking();
        booking.setFullName("John Doe");
        booking.setMobileNumber("1234567890");
        booking.setSpot(spot);
        booking.setVehicle(vehicle);
        booking.setUserId(userId);  // Make sure the userId is set before saving
        booking.setStatus(Booking.Status.CONFIRMED);


        // Create a Model object (since the controller expects it)
        Model model = mock(Model.class);
        // Directly verify the booking before calling saveBooking
        assertEquals(userId, booking.getUserId(), "User ID should match the mocked value.");
        // Create RedirectAttributes mock to capture the flash attributes
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // Simulate an exception (e.g., a spot is not available or any other exception in the booking process)
        lenient().when(spotRepository.findById(spotId)).thenThrow(new IllegalArgumentException("Invalid parking spot ID"));

        // Call the controller method
        String viewName = bookingController.bookParkingSpot(
                "John Doe",
                spotId.toString(),
                2,
                "2023-11-16T10:00:00",
                "40.00",
                "Credit Card",
                "1234567890",
                vehicleId,
                model,  // Passing Model object here
                redirectAttributes
        );

        // Verify the returned view
        assertEquals("redirect:/booking", viewName, "The view name should be a redirect to /booking");

        // Verify that the error message was added to redirectAttributes due to the exception
        verify(redirectAttributes).addFlashAttribute("errorMessage", "Failed to confirm booking: Invalid parking spot ID");

        // Ensure that no booking was saved due to the exception (no interaction with the booking service)
        verify(bookingService, times(0)).saveBooking(Mockito.any(Booking.class));

        // Ensure that no spot update happened due to the exception (no interaction with spotRepository)
        verify(spotRepository, times(0)).save(spot);


        // Verify that saveBooking was called with the correct Booking object
        //   verify(bookingService).saveBooking(Mockito.any(Booking.class));  // Just verify the call, not the specific captured object

        // Optionally check if other fields match
        assertEquals("John Doe", booking.getFullName(), "Full name should match");
        assertEquals("1234567890", booking.getMobileNumber(), "Mobile number should match");
        assertEquals(spot, booking.getSpot(), "The spot should match the mocked spot");
        assertEquals(vehicle, booking.getVehicle(), "The vehicle should match the mocked vehicle");
        assertEquals(Booking.Status.CONFIRMED, booking.getStatus(), "The booking status should be CONFIRMED");

    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testBookingHistory() {
        // Create a User and set a valid ID
        User user = new User();
        user.setId(1L); // Set a valid user ID

        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking());

        // Mocking behavior
        Mockito.lenient().when(userService.getLoggedInUser()).thenReturn(user); // Mock logged-in user
        // when(bookingService.findBookingsByUserId(user.getId())).thenReturn(bookings); // Mock booking retrieval
        Mockito.lenient().when(bookingService.findBookingsByUserId(anyLong())).thenReturn(bookings);

        // Call the controller method
        String viewName = bookingController.viewBookingDetails(model);

        // Verify interactions and assertions
        verify(model).addAttribute("bookings", bookings); // Verify model attribute
        assertEquals("booking/view-booking", viewName); // Verify view name
    }
    @Test
    void testBookParkingSpotSuccess() {
        // Prepare mock data
        Long userId = 1L;
        String fullName = "John Doe";
        String parkingSpotId = "101";
        int duration = 2;
        String startTime = "2024-12-30T10:00:00";
        String price = "40.00";
        String paymentMethod = "Cash";
        String mobileNumber = "1234567890";
        Long vehicleId = 1L;

        Spot spot = new Spot();
        spot.setSpotId(101L);
        spot.setPricePerHour(20.0);
        spot.setLocation("Downtown");
        spot.setAvailability("YES");

        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleId);
        vehicle.setVehicleModel("Toyota Camry");
        vehicle.setVehicleNumber("XYZ1234");

        // Mock external dependencies
        when(userService.findUserIdByUsername(anyString())).thenReturn(userId);
        when(spotRepository.findById(Long.valueOf(parkingSpotId))).thenReturn(java.util.Optional.of(spot));
        when(vehicleRepository.findById(vehicleId)).thenReturn(java.util.Optional.of(vehicle));

        // Call the method under test
        String viewName = bookingController.bookParkingSpot(fullName, parkingSpotId, duration, startTime, price, paymentMethod, mobileNumber, vehicleId, model, redirectAttributes);

        // Verify interactions and state changes
        verify(userService).findUserIdByUsername(anyString());
        verify(spotRepository).findById(Long.valueOf(parkingSpotId));
        verify(vehicleRepository).findById(vehicleId);
        verify(bookingService).saveBooking(any(Booking.class));
        verify(spotRepository).save(spot);

        // Assert that the correct view name is returned
        assertEquals("redirect:/booking", viewName);

        // Verify that the redirect message is added
        verify(redirectAttributes).addFlashAttribute("confirmationMessage", "Booking Confirmed! Your spot is reserved.");
    }

    @Test
    void testBookParkingSpotFailure() {
        // Prepare mock data
        Long userId = 1L;
        String fullName = "John Doe";
        String parkingSpotId = "101";
        int duration = 2;
        String startTime = "2024-12-30T10:00:00";
        String price = "40.00";
        String paymentMethod = "Cash";
        String mobileNumber = "1234567890";
        Long vehicleId = 1L;

        // Mocking invalid Spot and Vehicle repository behavior (e.g., Spot not found)
        when(userService.findUserIdByUsername(anyString())).thenReturn(userId);
        when(spotRepository.findById(Long.valueOf(parkingSpotId))).thenReturn(java.util.Optional.empty()); // Simulating Spot not found
        //   when(vehicleRepository.findById(vehicleId)).thenReturn(java.util.Optional.of(new Vehicle()));

        // Call the method under test and expect a failure
        String viewName = bookingController.bookParkingSpot(fullName, parkingSpotId, duration, startTime, price, paymentMethod, mobileNumber, vehicleId, model, redirectAttributes);

        // Verify that the error message is set
        verify(redirectAttributes).addFlashAttribute(eq("errorMessage"), anyString());

        // Assert that the view name indicates a failure
        assertEquals("redirect:/booking", viewName);
    }

}