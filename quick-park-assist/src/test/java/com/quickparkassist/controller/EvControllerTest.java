package com.quickparkassist.controller;

import com.quickparkassist.model.Evmodel;
import com.quickparkassist.model.Spot;
import com.quickparkassist.model.User;
import com.quickparkassist.model.Vehicle;
import com.quickparkassist.repository.VehicleRepository;
import com.quickparkassist.service.EvService;
import com.quickparkassist.service.SpotService;
import com.quickparkassist.service.UserServiceImpl;
import com.quickparkassist.service.EVChargingStationService;
import com.quickparkassist.util.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EvControllerTest {

    @InjectMocks
    private EvController evController;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private SpotService spotService;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private EvService evService;

    @Mock
    private EVChargingStationService chargingStationService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    private User testUser;
    private Evmodel testReservation;
    private Spot testSpot;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setId(1L);

        testReservation = new Evmodel();
        testReservation.setId(1L);
        testReservation.setStart_time("10:00");
        testReservation.setEvSpot(1L);

        testSpot = new Spot();
        testSpot.setSpotId(1L);
        testSpot.setSpotName("Test Spot");
        testSpot.setLocation("Test Location");
    }

    @Test
    void testInitBinder() {
        // This test case is designed to ensure the initBinder method runs without throwing exceptions
        //evController.initBinder(null);
        WebDataBinder binder = new WebDataBinder(null);  // Initialize the binder
        evController.initBinder(binder);

        // Optionally, you can verify if the custom editor was registered
        assertNotNull(binder.findCustomEditor(Date.class, null));
    }

    @Test
    void testCreateReservationForm_UserLoggedIn() {
        // Mock UserDetails instead of User
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                testUser.getEmail(),
                "password",  // Mock password
                new ArrayList<>()
        );

        when(userService.getUserByEmail("test@example.com")).thenReturn(testUser);
        when(vehicleRepository.findByUserEmailAndHasElectric("test@example.com", "YES")).thenReturn(Collections.emptyList());
        when(spotService.getSpotsByType("ev")).thenReturn(Collections.singletonList(testSpot));

        String viewName = evController.createReservationForm(model, userDetails);

        verify(model).addAttribute("user", testUser);
        verify(model).addAttribute("vehicles", Collections.emptyList());
        verify(model).addAttribute("evSpots", Collections.singletonList(testSpot));
        assertEquals("reservations/create", viewName);
    }

    @Test
    void testCreateReservationForm_UserNotLoggedIn() {
        when(userService.getUserByEmail(any())).thenReturn(null);

        String viewName = evController.createReservationForm(model, (UserDetails) null);

        verify(model).addAttribute("user", null);
        verify(model).addAttribute("vehicles", Collections.emptyList());
        assertEquals("reservations/create", viewName);
    }

        @Test
        void testViewAllReservations_UserFound() {
            when(userService.findUserIdByUsername("test@example.com")).thenReturn(testUser.getId());
            when(evService.getAllReservations()).thenReturn(new ArrayList<>());

            String viewName = evController.viewAllReservations(model);

            verify(model).addAttribute("reservations", Collections.emptyList());
            assertEquals("reservations/view", viewName);
        }

@Test
void testViewAllReservations_UserNotFound() {
    // Mock UserDetails
    UserDetails mockUserDetails = new org.springframework.security.core.userdetails.User(
            "testuser@example.com", // Username
            "password",             // Password
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // Authorities
    );

    // Mock SecurityContext and Authentication
    SecurityContext securityContext = mock(SecurityContext.class);
    Authentication authentication = mock(Authentication.class);

    when(authentication.getPrincipal()).thenReturn(mockUserDetails);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);

    // Mock the userService to return null for the user ID
    when(userService.findUserIdByUsername("testuser@example.com")).thenReturn(null);

    // Call the controller method
    String viewName = evController.viewAllReservations(model);

    // Verify that the model contains an empty list of reservations
    verify(model).addAttribute("reservations", new ArrayList<>());
    assertEquals("reservations/view", viewName); // Adjust the view as necessary
}


//    @Test
//    void testViewAllReservations_UserNotFound() {
//     //   when(UserContext.getCurrentUsername()).thenReturn(testUser.getEmail());
//        when(userService.findUserIdByUsername(testUser.getEmail())).thenReturn(1L);
//        // Set userId for the test reservation to avoid NullPointerException
//        testReservation.setUserId(1L);
//        when(evService.getAllReservations()).thenReturn(Collections.singletonList(testReservation));
//
//        String viewName = evController.viewAllReservations(model);
//
//        //verify(model).addAttribute("reservations", new ArrayList<>());
//        verify(model).addAttribute("reservations", Collections.singletonList(testReservation));
//
//        assertEquals("reservations/view", viewName); // Adjust the view as necessary
//    }

    @Test
    void testSaveReservation_Success() {
        Evmodel reservation = new Evmodel();
        reservation.setStart_time("10:00");
        reservation.setEvSpot(1L);

        when(userService.findUserIdByUsername("test@example.com")).thenReturn(testUser.getId());
        when(spotService.getSpotById(1L)).thenReturn(testSpot);
        when(evService.saveReservation(any(Evmodel.class))).thenReturn(reservation);

        String viewName = evController.saveReservation(reservation, redirectAttributes);

        assertEquals("redirect:/reservations", viewName);
        verify(redirectAttributes).addFlashAttribute("message", "Your charging slot has been successfully reserved.");
    }

    @Test
    void testSaveReservation_SpotNotFound() {
        Evmodel reservation = new Evmodel();
        reservation.setEvSpot(1L);
        reservation.setStart_time("10:00");
        when(userService.findUserIdByUsername("test@example.com")).thenReturn(testUser.getId());
        when(spotService.getSpotById(1L)).thenReturn(null);

        String viewName = evController.saveReservation(reservation, redirectAttributes);

        assertEquals("redirect:/reservations", viewName);
        verify(redirectAttributes).addFlashAttribute("errorMessage", "Selected EV spot not found.");
    }

    @Test
    void testEditReservationForm() {
        Long reservationId = 1L;
        Evmodel reservation = new Evmodel();
        UserDetails loggedInUser = mock(UserDetails.class);
        User user = new User();
        user.setEmail("test@example.com");

        Vehicle electricVehicle = new Vehicle();
        electricVehicle.setHasElectric("YES");

        List<Vehicle> electricVehicles = Collections.singletonList(electricVehicle);
        List<Spot> evSpots = Collections.singletonList(testSpot);

        when(evService.getReservationById(reservationId)).thenReturn(reservation);
        when(loggedInUser.getUsername()).thenReturn("test@example.com");
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);
        when(vehicleRepository.findByUserEmailAndHasElectric("test@example.com", "YES"))
                .thenReturn(electricVehicles);
        when(spotService.getSpotsByType("ev")).thenReturn(evSpots);

        String viewName = evController.editReservationForm(reservationId, model, loggedInUser);

        verify(model).addAttribute("reservation", reservation);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("vehicles", electricVehicles);
        verify(model).addAttribute("evSpots", evSpots);
        assertEquals("reservations/update", viewName);
    }

    @Test
    void testUpdateReservation_Success() {
        Evmodel reservation = new Evmodel();
        reservation.setEvSpot(1L);
        reservation.setStart_time("10:00");

        when(userService.findUserIdByUsername("test@example.com")).thenReturn(testUser.getId());
        when(spotService.getSpotById(1L)).thenReturn(testSpot);
        when(evService.saveReservation(any(Evmodel.class))).thenReturn(reservation);

        String viewName = evController.updateReservation(reservation);

        assertEquals("redirect:/reservations/edit_ev", viewName);
    }

    @Test
    void testUpdateReservation_UserNotFound() {
        Evmodel reservation = new Evmodel();
        reservation.setEvSpot(1L);
        reservation.setStart_time("10:00");

        when(userService.findUserIdByUsername("test@example.com")).thenReturn(null);

        String viewName = evController.updateReservation(reservation);

        assertEquals("redirect:/reservations/edit_ev", viewName); // Redirect to login or error page
    }

    @Test
    void testDeleteReservation() {
        Long reservationId = 1L;

        String viewName = evController.deleteReservation(reservationId);

        assertEquals("redirect:/reservations/delete_ev", viewName);
        verify(evService).deleteReservation(reservationId);
    }

    @Test
    void testEditEv() {
        String viewName = evController.edit_ev(model);

        assertEquals("reservations/edit_page", viewName);
    }

    @Test
    void testDeleteEv() {
        String viewName = evController.delete_ev(model);

        assertEquals("reservations/delete_page", viewName);
    }


}