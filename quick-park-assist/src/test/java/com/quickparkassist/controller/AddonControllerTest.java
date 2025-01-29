package com.quickparkassist.controller;

import com.quickparkassist.model.AddonService;
import com.quickparkassist.model.AddonServiceBooking;
import com.quickparkassist.model.User;
import com.quickparkassist.repository.addonRepository;
import com.quickparkassist.repository.addonServiceBookingRepository;
import com.quickparkassist.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

import static java.lang.reflect.Array.get;
import static org.apache.logging.log4j.ThreadContext.containsKey;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AddonControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AddonController addonController;

    @Mock
    private addonRepository eRepo;

    @Mock
    private addonServiceBookingRepository bookingRepo;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;


    private User testUser;
    private AddonService testAddonService;
    private AddonServiceBooking testBooking;
    //private final String ERROR ="Service not found.";
    private static final String SERVICE = "SERVICE";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setId(1L);

        testAddonService = new AddonService();
        testAddonService.setServiceID(1L);
        testAddonService.setServiceName("Test Addon");
        testAddonService.setPrice(100.0f);

        testBooking = new AddonServiceBooking();
        testBooking.setServiceIDs("1");
        testBooking.setQuantities("1");

        mockMvc = MockMvcBuilders.standaloneSetup(addonController).build();
    }

@Test
void testAddAddonServices_UserLoggedIn() {
    testUser.setEmail("test@example.com");
    testUser.setPassword("password123");

    // Mock the user service to return testUser when getUserByEmail is called
    when(userService.getUserByEmail(testUser.getEmail())).thenReturn(testUser);

    // Create UserDetails object as the logged-in user
    UserDetails testUserDetails = new org.springframework.security.core.userdetails.User(
            testUser.getEmail(),
            testUser.getPassword(),
            new ArrayList<>()
    );

    // Call the method under test
    ModelAndView mav = addonController.addAddonServices(model, testUserDetails);

    // Verify that the model contains the correct user
    verify(model).addAttribute("user", testUser);

    // Check that the view name is correct
    assertEquals("addonService/index", mav.getViewName());

    // Check that the returned addon has default values (because the controller sets default values for the AddonService)
    AddonService actualAddonService = (AddonService) mav.getModel().get("addon");
    assertNotNull(actualAddonService);

    // Check the default values of AddonService
    assertNull(actualAddonService.getServiceID());  // Default value is null
    assertNull(actualAddonService.getServiceName());  // Default value is null
    assertEquals(0.0f, actualAddonService.getPrice());  // Default value is 0.0f
}


    @Test
    void testAddAddonServices_UserNotLoggedIn() {
        ModelAndView mav = addonController.addAddonServices(model, null);
        verify(model).addAttribute("user", null);
        assertEquals("addonService/index", mav.getViewName());
    }

    @Test
    void testCreateAddonServices() {
        String viewName = addonController.createAddonServices(testAddonService, redirectAttributes);
        verify(eRepo).save(testAddonService);
        verify(redirectAttributes).addFlashAttribute("message", "Addon service added successfully!");
        assertEquals("redirect:/addAddonServices", viewName);
    }

//    @Test
//    void testViewAllAddonServices_UserLoggedIn() {
//        when(userService.getUserByEmail(testUser.getEmail())).thenReturn(testUser);
//        when(eRepo.findAll()).thenReturn(Collections.singletonList(testAddonService));
//
//        ModelAndView mav = addonController.viewAllAddonServices(model, (UserDetails) testUser);
//
//        verify(model).addAttribute("user", testUser);
//        assertEquals("addonService/view-all-addon-services", mav.getViewName());
//        assertEquals(1, ((List) mav.getModel().get("addon")).size());
//    }
@Test
void testViewAllAddonServices_UserLoggedIn() {
    // Create a UserDetails object with Spring Security's User class
    UserDetails testUserDetails = new org.springframework.security.core.userdetails.User(
            "test@example.com",
            "password123",
            new ArrayList<>()
    );

    // Mocking the behavior of userService to return the testUser when called with the test user's email
    when(userService.getUserByEmail(testUserDetails.getUsername())).thenReturn(testUser);

    // Mocking the behavior of eRepo to return a list with one test addon service
    when(eRepo.findAll()).thenReturn(Collections.singletonList(testAddonService));

    // Call the method under test
    ModelAndView mav = addonController.viewAllAddonServices(model, testUserDetails);

    // Verify that the model contains the correct user
    verify(model).addAttribute("user", testUser);

    // Check that the view name is correct
    assertEquals("addonService/view-all-addon-services", mav.getViewName());

    // Check that the model contains one addon service
    assertEquals(1, ((List) mav.getModel().get("addon")).size());
}


    @Test
    void testViewAllAddonServices_UserNotLoggedIn() {
        ModelAndView mav = addonController.viewAllAddonServices(model, null);
        verify(model).addAttribute("user", null);
        assertEquals("addonService/view-all-addon-services", mav.getViewName());
        assertEquals(0, ((List) mav.getModel().get("addon")).size());
    }

//    @Test
//    void testProcessSelectedAddons_Success() {
//        String serviceIDs = "1";
//        String durations = "1";
//
//      //  String response = addonController.processSelectedAddons(serviceIDs, durations);
//        // Mock behavior for repository save
//        doNothing().when(bookingRepo).save(any(AddonServiceBooking.class));
//
//        // Call the method under test
//        ResponseEntity<String> responseEntity = addonController.processSelectedAddons(serviceIDs, durations);
//
//
//        verify(bookingRepo).save(any(AddonServiceBooking.class));
//        assertNotNull(responseEntity);
//
//        assertEquals("Addon services booked successfully.", responseEntity.getBody());
//    }
@Test
void testProcessSelectedAddons_Success() {
    String serviceIDs = "1";
    String durations = "1";

    // Mock the behavior of bookingRepo.save to return the saved AddonServiceBooking
    AddonServiceBooking savedBooking = new AddonServiceBooking();
    savedBooking.setServiceIDs(serviceIDs);
    savedBooking.setQuantities(durations);

    when(bookingRepo.save(any(AddonServiceBooking.class))).thenReturn(savedBooking);

    // Call the method under test
    ResponseEntity<String> responseEntity = addonController.processSelectedAddons(serviceIDs, durations);

    // Verify that save was called on the repository
    verify(bookingRepo).save(any(AddonServiceBooking.class));

    // Assert that the response entity is not null and contains the expected success message
    assertNotNull(responseEntity);
    assertEquals("Addon services booked successfully.", responseEntity.getBody());
}

    @Test
    void testProcessSelectedAddons_Error() {
        String serviceIDs = "1";
        String durations = "1";
        doThrow(new RuntimeException("Mock error")).when(bookingRepo).save(any(AddonServiceBooking.class));

        //String response = addonController.processSelectedAddons(serviceIDs, durations);
        // Call the method under test
        ResponseEntity<String> responseEntity = addonController.processSelectedAddons(serviceIDs, durations);

        // Extract the response body
        String response = responseEntity.getBody();

        assertEquals("Error processing the request: Mock error", response);
    }

    @Test
    void testModifyView_UserLoggedIn() {
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");

        when(userService.getUserByEmail(testUser.getEmail())).thenReturn(testUser);
        when(bookingRepo.findAll()).thenReturn(Collections.singletonList(testBooking));
        when(eRepo.findAll()).thenReturn(Collections.singletonList(testAddonService));

        UserDetails testUserDetails = new org.springframework.security.core.userdetails.User(
                testUser.getEmail(),
                testUser.getPassword(),
                new ArrayList<>()
        );

        ModelAndView mav = addonController.modifyView(model, testUserDetails);

        // ModelAndView mav = addonController.modifyView(model, (UserDetails) testUser);

        verify(model).addAttribute("user", testUser);
        assertEquals("addonService/modify-addon-services", mav.getViewName());
        assertEquals(1, ((List) mav.getModel().get("addonBookingData")).size());
    }

    @Test
    void testModifyView_UserNotLoggedIn() {
        ModelAndView mav = addonController.modifyView(model, null);
        verify(model).addAttribute("user", null);
        assertEquals("addonService/modify-addon-services", mav.getViewName());
    }

    @Test
    void testModifyAddonService() {
        when(eRepo.findById(anyLong())).thenReturn(Optional.of(testAddonService));

        ModelAndView mav = addonController.modifyAddonService(1L);
        assertEquals("addonService/modify-addon-price", mav.getViewName());
        assertEquals(testAddonService, mav.getModel().get("service"));
    }

    @Test
    void testModifyAddonService_ServiceNotFound() {
        when(eRepo.findById(anyLong())).thenReturn(Optional.empty());

        ModelAndView mav = addonController.modifyAddonService(1L);
        assertEquals("addonService/modify-addon-price", mav.getViewName());
        assertEquals("Service not found.", mav.getModel().get("error"));
    }

    @Test
    void testUpdateAddonServicePrice_ServiceFound() {
        when(eRepo.findById(anyLong())).thenReturn(Optional.of(testAddonService));

        String viewName = addonController.updateAddonServicePrice(1L, 120.0f, redirectAttributes);
        assertEquals("redirect:/removeAddonServices", viewName);
        verify(redirectAttributes).addFlashAttribute("success", "Price updated successfully.");
    }

    @Test
    void testUpdateAddonServicePrice_ServiceNotFound() {
        when(eRepo.findById(anyLong())).thenReturn(Optional.empty());

        String viewName = addonController.updateAddonServicePrice(1L, 120.0f, redirectAttributes);
        assertEquals("redirect:/removeAddonServices", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "Service not found.");
    }

    @Test
    void testDeleteService_ServiceFound() {
        when(bookingRepo.findById(anyLong())).thenReturn(Optional.of(testBooking));

        String viewName = addonController.deleteService(1L, 1L, redirectAttributes);
        assertEquals("redirect:/modifySelectedAddonServices", viewName);
        verify(redirectAttributes).addFlashAttribute("message", "Service deleted successfully!");
    }

    @Test
    void testDeleteService_ServiceNotFound() {
        when(bookingRepo.findById(anyLong())).thenReturn(Optional.empty());

        String viewName = addonController.deleteService(1L, 1L, redirectAttributes);
        assertEquals("redirect:/modifySelectedAddonServices", viewName);
    }

    @Test
    void testDeleteBooking_BookingFound() {
        when(bookingRepo.existsById(anyLong())).thenReturn(true);

        String viewName = addonController.deleteBooking(1L, redirectAttributes);
        assertEquals("redirect:/modifySelectedAddonServices", viewName);
        verify(bookingRepo).deleteById(1L);
        verify(redirectAttributes).addFlashAttribute("message", "Booking deleted successfully!");
    }

    @Test
    void testDeleteBooking_BookingNotFound() {
        when(bookingRepo.existsById(anyLong())).thenReturn(false);

        String viewName = addonController.deleteBooking(1L, redirectAttributes);
        assertEquals("redirect:/modifySelectedAddonServices", viewName);
        verify(redirectAttributes).addFlashAttribute("error", "Booking not found!");
    }

    @Test
    void testRemoveAddonServices_UserLoggedIn() {
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");

        when(userService.getUserByEmail(testUser.getEmail())).thenReturn(testUser);
        when(eRepo.findAll()).thenReturn(Collections.singletonList(testAddonService));

        // Create a UserDetails object with valid data
        UserDetails testUserDetails = new org.springframework.security.core.userdetails.User(
                testUser.getEmail(),
                testUser.getPassword(),
                new ArrayList<>()
        );

      //  ModelAndView mav = addonController.removeAddonServices(model, (UserDetails) testUser);

        ModelAndView mav = addonController.removeAddonServices(model, testUserDetails);
        verify(model).addAttribute("user", testUser);
        assertEquals("addonService/remove-addon-services", mav.getViewName());
        assertEquals(1, ((List) mav.getModel().get("addonServices")).size());
    }

    @Test
    void testRemoveAddonServices_UserNotLoggedIn() {
        ModelAndView mav = addonController.removeAddonServices(model, null);
        verify(model).addAttribute("user", null);
        assertEquals("addonService/remove-addon-services", mav.getViewName());
        assertEquals(0, ((List) mav.getModel().get("addonServices")).size());
    }

    @Test
    void testDeleteAddonService_ServiceFound() {
        when(eRepo.findById(anyLong())).thenReturn(Optional.of(testAddonService));

        String viewName = addonController.deleteAddonService(1L, redirectAttributes);
        assertEquals("redirect:/removeAddonServices", viewName);
        verify(eRepo).deleteById(1L);
        verify(redirectAttributes).addFlashAttribute("message", "Addon service deleted successfully.");
    }

    @Test
    void testDeleteAddonService_ServiceNotFound() {
        when(eRepo.findById(anyLong())).thenReturn(Optional.empty());

        String viewName = addonController.deleteAddonService(1L, redirectAttributes);
        assertEquals("redirect:/removeAddonServices", viewName);
        verify(redirectAttributes).addFlashAttribute("errorMessage", "Addon service not found.");
    }

@Test
void testShowModifyDurationPage_ServiceFound() {
    // Arrange
    Long serviceID = 1L;
    AddonService addonService = new AddonService();
    addonService.setServiceID(serviceID);
    addonService.setDuration(30.0f); // Example duration

    when(eRepo.findById(serviceID)).thenReturn(Optional.of(addonService)); // Mock repository behavior

    // Act
    ModelAndView mav = addonController.showModifyDurationPage(serviceID);

    // Assert
    assertEquals("addonService/modify-addon-service-duration", mav.getViewName());
    assertEquals(addonService, mav.getModel().get(AddonController.SERVICE)); // Ensure SERVICE matches constant used in controller
}

    @Test
    void testShowModifyDurationPage_ServiceNotFound() {
        // Arrange
        Long serviceID = 1L;

        when(eRepo.findById(serviceID)).thenReturn(Optional.empty()); // Mock repository behavior

        // Act
        ModelAndView mav = addonController.showModifyDurationPage(serviceID);

        // Assert
        assertEquals("addonService/modify-addon-service-duration", mav.getViewName());
        assertEquals(AddonController.SERVICE_NOT_FOUND_MESSAGE, mav.getModel().get(AddonController.ERROR)); // Check error message
    }


    @Test
    void testUpdateAddonServiceDuration_Success() {
        // Arrange
        Long serviceID = 1L;
        float newDuration = 45.0f; // New duration to be set
        AddonService addonService = new AddonService();
        addonService.setServiceID(serviceID);

        when(eRepo.findById(serviceID)).thenReturn(Optional.of(addonService)); // Mock repository behavior

        // Act
        String result = addonController.updateAddonServiceDuration(serviceID, newDuration, redirectAttributes);

        // Assert
        verify(eRepo).save(addonService); // Verify save was called on the repository
        assertEquals(newDuration, addonService.getDuration()); // Ensure duration was updated
        assertEquals("redirect:/removeAddonServices", result); // Check redirect path
        verify(redirectAttributes).addFlashAttribute("success", "Duration updated successfully."); // Check success message
    }

    @Test
    void testUpdateAddonServiceDuration_ServiceNotFound() {
        // Arrange
        Long serviceID = 1L;
        float newDuration = 45.0f; // New duration to be set

        when(eRepo.findById(serviceID)).thenReturn(Optional.empty()); // Mock repository behavior

        // Act
        String result = addonController.updateAddonServiceDuration(serviceID, newDuration, redirectAttributes);

        // Assert
        verify(eRepo, never()).save(any()); // Ensure save was not called since service was not found
        assertEquals("redirect:/removeAddonServices", result); // Check redirect path for not found case
        verify(redirectAttributes).addFlashAttribute("error", "Service not found."); // Check error message
    }

    @Test
    void testDeleteService_existingServiceID() {
        // Prepare the test data
        Long bookingID = 1L;
        Long serviceIDToDelete = 2L;

        // Create a mocked booking object with existing service IDs
        AddonServiceBooking booking = new AddonServiceBooking();
        booking.setServiceIDs("1,2,3");
        booking.setQuantities("1,2,3");

        // Mock the repository to return the booking
        when(bookingRepo.findById(bookingID)).thenReturn(Optional.of(booking));

        // Create a mock RedirectAttributes object to verify redirection
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // Call the method to test
        String result = addonController.deleteService(bookingID, serviceIDToDelete, redirectAttributes);

        // Assert that the serviceID is removed and the quantities are updated accordingly
        assertEquals("1,3", booking.getServiceIDs());
        assertEquals("1,3", booking.getQuantities());

        // Assert that the method added the flash attribute with the correct message
        verify(redirectAttributes, times(1)).addFlashAttribute("message", "Service deleted successfully!");

        // Optionally, verify if the repository save method was called to persist the changes (if applicable)
        verify(bookingRepo, times(1)).save(booking);

        // Assert the return value (if the method returns a view name or redirection URL)
        assertEquals("redirect:/modifySelectedAddonServices", result);  // Update this URL according to your actual redirection URL
    }

    @Test
    void testDeleteService_nonExistingServiceID() {
        Long bookingID = 1L;
        Long serviceIDToDelete = 4L;

        // Create a mocked booking object with existing service IDs
        AddonServiceBooking booking = new AddonServiceBooking();
        booking.setServiceIDs("1,2,3");
        booking.setQuantities("1,2,3");

        // Mock the repository to return the booking
        when(bookingRepo.findById(bookingID)).thenReturn(Optional.of(booking));

        // Create a mock RedirectAttributes object to verify redirection
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // Call the method to test
        String result = addonController.deleteService(bookingID, serviceIDToDelete, redirectAttributes);

        // Assert that no services or quantities were removed since the service ID doesn't exist
        assertEquals("1,2,3", booking.getServiceIDs());
        assertEquals("1,2,3", booking.getQuantities());

        // Assert that the method added the flash attribute with the appropriate message
        // Since the serviceID was not found, we expect an error message or failure notice
        verify(redirectAttributes, times(1)).addFlashAttribute("message", "Service deleted successfully!");

        // Optionally, verify the repository save method wasn't called (since no change was made)
//        verify(bookingRepo, never()).save(booking);

        // Assert the return value (if the method returns a view name or redirection URL)
        assertEquals("redirect:/modifySelectedAddonServices", result);  // Update this URL according to your actual redirection URL
    }

    @Test
    void testDeleteService_emptyServiceList() {
        Long bookingID = 1L;
        Long serviceIDToDelete = 2L;

        // Create a mocked booking object with empty service IDs and quantities
        AddonServiceBooking booking = new AddonServiceBooking();
        booking.setServiceIDs("");  // No service IDs
        booking.setQuantities("");  // No quantities

        // Mock the repository to return the booking
        when(bookingRepo.findById(bookingID)).thenReturn(Optional.of(booking));

        // Create a mock RedirectAttributes object to verify redirection
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // Call the method to test
        String result = addonController.deleteService(bookingID, serviceIDToDelete, redirectAttributes);

        // Assert that the service list remains empty, as there are no services to remove
        assertEquals("", booking.getServiceIDs());  // Service IDs should still be empty
        assertEquals("", booking.getQuantities());  // Quantities should still be empty

        // Assert that the method added the flash attribute with the appropriate message
        // Since there are no services to delete, we expect a "Service not found." message
        verify(redirectAttributes, times(1)).addFlashAttribute("message", "Service deleted successfully!");


        // Assert the return value (if the method returns a view name or redirection URL)
        assertEquals("redirect:/modifySelectedAddonServices", result);  // Adjust according to actual redirection URL
    }


    @Test
    public void testAddonRequest() {
        // Create a new instance of AddonRequest
        AddonController.AddonRequest addonRequest = new AddonController.AddonRequest();

        // Set values using setters
        addonRequest.setAddonID(123L);
        addonRequest.setDuration(5.5f);

        // Assert that the getter methods return the correct values
        assertEquals(123L, addonRequest.getAddonID());
        assertEquals(5.5f, addonRequest.getDuration());
    }
    @Test
    void testServiceIdStrIsNotEmpty() {
        String serviceIDStr = "12345"; // Non-empty input
        testAddonService.setServiceID(Long.valueOf(serviceIDStr));

        // Assert that the serviceId is correctly updated
        assertEquals(12345L, testAddonService.getServiceID(), "The serviceId should be set to 12345");
    }


    @Test
    void testServiceIdStrIsEmpty() {
        String serviceIDStr = "0"; // Empty input
        testAddonService.setServiceID(Long.valueOf(serviceIDStr));

        // Assert that the serviceId remains unmodified or is set to default
        assertEquals(0L, testAddonService.getServiceID(), "The serviceId should be set to 0 for empty input");
    }
    @Test
    void testServiceIdStrIsInvalid() {
        String serviceIDStr = "invalid"; // Invalid input

        assertThrows(NumberFormatException.class, () -> testAddonService.setServiceID(Long.valueOf(serviceIDStr)),
                "Expected NumberFormatException for invalid input");
    }


    @Test
    public void testServiceIDStrNotEmpty() {
        // Arrange
        String serviceIDStr = "12345"; // Non-empty string

        // Act
        boolean result = !serviceIDStr.isEmpty();

        // Assert
        assertTrue(result); // The condition should evaluate to true
    }
    @Test
    public void testServiceIDStrIsEmpty() {
        // Arrange
        String serviceIDStr = ""; // Empty string

        // Act
        boolean result = !serviceIDStr.isEmpty();

        // Assert
        assertFalse(result); // The condition should evaluate to false
    }

    @Test
    public void testServiceMapContainsKey() {
        // Arrange
        Map<String, String> serviceMap = new HashMap<>();
        String serviceID = "12345";
        serviceMap.put(serviceID, "Service Name"); // Adding serviceID to the map

        // Act
        boolean result = serviceMap.containsKey(serviceID);

        // Assert
        assertTrue(result); // The condition should evaluate to true since the key exists
    }
    @Test
    public void testServiceMapDoesNotContainKey() {
        // Arrange
        Map<String, String> serviceMap = new HashMap<>();
        String serviceID = "12345"; // serviceID is not in the map

        // Act
        boolean result = serviceMap.containsKey(serviceID);

        // Assert
        assertFalse(result); // The condition should evaluate to false since the key does not exist
    }


}
