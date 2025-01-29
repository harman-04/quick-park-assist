package com.quickparkassist.controller;

import com.quickparkassist.model.Spot;
import com.quickparkassist.model.User;
import com.quickparkassist.repository.SpotRepository;
import com.quickparkassist.service.SpotService;
import com.quickparkassist.service.UserService;
import com.quickparkassist.service.UserServiceImpl;
import com.quickparkassist.util.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
 class TestSpotController {

    private MockMvc mockMvc;

    @Mock
    private SpotService spotService;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private SpotController spotController;

     @Mock
     private Model model; // Mocked Model

     @Mock
     private UserDetails loggedInUser; // Mocked UserDetails

@Mock
private SpotRepository spotRepository;

     private Spot spot;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(spotController).build();
        user = new User();
        user.setEmail("ravi@gmail.com");

        spot = new Spot();
        spot.setSpotId(1L);
        spot.setLocation("India");
    }

     @Test
     void testHome() {
         User mockUser = new User();
         mockUser.setEmail("ravi@gmail.com");

         // Mock UserDetails
         UserDetails mockUserDetails = mock(UserDetails.class);
         when(mockUserDetails.getUsername()).thenReturn("ravi@gmail.com");

         when(userService.getUserByEmail("ravi@gmail.com")).thenReturn(mockUser);
         when(spotService.getAvailableSpots()).thenReturn(Collections.emptyList());
         when(spotService.getUnavailableSpots()).thenReturn(Collections.emptyList());

         String view = spotController.home(model, mockUserDetails);

         verify(model).addAttribute("user", mockUser);
         verify(model).addAttribute("availableSpots", Collections.emptyList());
         verify(model).addAttribute("unavailableSpots", Collections.emptyList());
         assertEquals("spotmanagement/addSpots", view);
     }
    @Test
    void testAddSpot_Success() throws Exception {
        when(userService.findUserIdByUsername("ravi@gmail.com")).thenReturn(1L);

        mockMvc.perform(post("/addSpot")
                        .param("location", "India")
                        .flashAttr("spot", spot))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/add"));

        verify(spotService, times(1)).saveSpot(any(Spot.class));
    }


    @Test
    void testAddSpot_MissingParameters() throws Exception {
        mockMvc.perform(post("/addSpot"))
                .andExpect(status().is3xxRedirection());
    }


    @Test
    void testSearchSpots_UserHasNoSpots() throws Exception {
        when(userService.findUserIdByUsername("ravi@gmail.com")).thenReturn(1L);
        when(spotService.getSpotsByLocationAndAvailability("Delhi", "Car", "yes"))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(post("/searchspots")
                        .param("location", "Delhi")
                        .param("spotType", "Car")
                        .param("availability", "yes"))
                .andExpect(status().isOk())
                .andExpect(view().name("spotmanagement/searchSpot"))
                .andExpect(model().attribute("message", "Please enter a spot registered by you."));

        verify(spotService, times(1)).getSpotsByLocationAndAvailability("Delhi", "Car", "yes");
    }


    @Test
    void testModifySpot_Success() throws Exception {
        mockMvc.perform(post("/modifySpot")
                        .flashAttr("spot", spot))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/edit"));

        verify(spotService, times(1)).updateSpot(any(Spot.class));
    }


    @Test
    void testModifySpot_MissingParameters() throws Exception {
        mockMvc.perform(post("/modifySpot"))
                .andExpect(status().is3xxRedirection());
    }


    @Test
    void testRemoveSpot_Success() throws Exception {
        mockMvc.perform(post("/removeSpot")
                        .param("id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/remove"));

        verify(spotService, times(1)).removeSpot(1L);
    }


    @Test
    void testRemoveSpot_InvalidId() throws Exception {
        mockMvc.perform(post("/removeSpot"))
                .andExpect(status().isBadRequest());
    }


     @Test
     void testHome_UserLoggedIn() {
         // Arrange
         String email = "test@example.com";
         User user = new User(); // Create a user object as needed

         when(loggedInUser.getUsername()).thenReturn(email);
         when(userService.getUserByEmail(email)).thenReturn(user);
         when(spotService.getAvailableSpots()).thenReturn(new ArrayList<>());
         when(spotService.getUnavailableSpots()).thenReturn(new ArrayList<>());

         // Act
         String result = spotController.home(model, loggedInUser);

         // Assert
         verify(model, times(1)).addAttribute("user", user); // Check if user is added to model
         verify(model, times(1)).addAttribute("availableSpots", new ArrayList<>()); // Check available spots
         verify(model, times(1)).addAttribute("unavailableSpots", new ArrayList<>()); // Check unavailable spots
         assertEquals("spotmanagement/addSpots", result); // Verify return view name is correct
     }

     @Test
     void testHome_UserNotLoggedIn() {
         // Act
         String result = spotController.home(model, null);

         // Assert
         verify(model, times(1)).addAttribute("user", null); // Check if user is null in model
         verify(model, times(1)).addAttribute("availableSpots", new ArrayList<>()); // Check available spots
         verify(model, times(1)).addAttribute("unavailableSpots", new ArrayList<>()); // Check unavailable spots
         assertEquals("spotmanagement/addSpots", result); // Verify return view name is correct
     }

     @Test
     void testAdding() {
         // Act
         String result = spotController.adding();

         // Assert
         assertEquals("spotmanagement/addSpots", result); // Verify return view name is correct
     }
//     @Test
//     void testAvailable() {
//         // Arrange
//         String email = "test@example.com";
//         Long userId = 1L;
//
//         when(loggedInUser.getUsername()).thenReturn(email);
//         when(userService.findUserIdByUsername(email)).thenReturn(userId);
//
//         // Initialize mock spot list
//         // Initialize mock spot list
//         List<Spot> userSpots = new ArrayList<>();
//         userSpots.add(new Spot(
//                 101L,                 // spotId
//                 "Available",          // availability
//                 "Test Location",      // location
//                 5,                    // slot
//                 10.5,                 // pricePerHour
//                 "Test Spot",          // spotName
//                 "Test Description",   // description
//                 "Active",             // spotStatus
//                 "Test Station",       // station
//                 "Car"                 // spotType
//         ));
//
//         when(spotService.getAvailableSpotsByUserId(userId)).thenReturn(userSpots);
//
//         // Act
//         String result = spotController.available(model);
//
//         // Assert
//         verify(model, times(1)).addAttribute("availableSpots", userSpots); // Check available spots for the user
//         assertEquals("spotmanagement/availableSpots", result); // Verify return view name is correct
//     }

//     @Test
//     void testAvailable() {
//         // Arrange
//         String email = "test@example.com";
//         Long userId = 1L;
//
//         when(loggedInUser.getUsername()).thenReturn(email);
//         when(userService.findUserIdByUsername(email)).thenReturn(userId);
//
//         List<Spot> userSpots = new ArrayList<>();
//         userSpots.add(new Spot(/* initialize as needed */));
//
//         when(spotService.getAvailableSpotsByUserId(userId)).thenReturn(userSpots);
//
//         // Act
//         String result = spotController.available(model);
//
//         // Assert
//         verify(model, times(1)).addAttribute("availableSpots", userSpots); // Check available spots for the user
//         assertEquals("spotmanagement/availableSpots", result); // Verify return view name is correct
//     }
//     @Test
//     void testEdit() {
//         // Create a mock UserDetails object
//       UserDetails mockUserDetails = new org.springframework.security.core.userdetails.User(
//                 "testuser@example.com",
//                 "password",
//                 Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
//         );
//         // Arrange
//         String email = mockUserDetails.getUsername();
//         Long userId = 1L;
//
//         when(userService.findUserIdByUsername(email)).thenReturn(userId);
//
//         // Initialize available spots with proper values
//         List<Spot> availableSpots = new ArrayList<>();
//         Spot availableSpot = new Spot();
//         availableSpot.setSpotId(1L); // Assuming there's an ID field
//         availableSpot.setLocation("Downtown");
//         availableSpots.add(availableSpot);
//
//         when(spotService.getAvailableSpotsByUser_Id(userId)).thenReturn(availableSpots);
//
//         // Initialize unavailable spots with proper values
//         List<Spot> unavailableSpots = new ArrayList<>();
//         Spot unavailableSpot = new Spot();
//         unavailableSpot.setSpotId(2L); // Assuming there's an ID field
//         unavailableSpot.setLocation("Uptown");
//         unavailableSpots.add(unavailableSpot);
//
//         when(spotService.getUnavailableSpotsByUserId(userId)).thenReturn(unavailableSpots);
//
//         // Act
//         String result = spotController.edit(model);
//
//         // Assert
//         verify(model, times(1)).addAttribute("availableSpots", availableSpots);
//         verify(model, times(1)).addAttribute("unavailableSpots", unavailableSpots);
//         assertEquals("spotmanagement/editSpots", result);
//     }

//     @Test
//     void testEdit() {
//         // Arrange
//         // Create a mock UserDetails object
//       UserDetails mockUserDetails = new org.springframework.security.core.userdetails.User(
//                 "testuser@example.com",
//                 "password",
//                 Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
//         );
//         String email = mockUserDetails.getUsername();
//         Long userId = 1L;
//
//         when(userService.findUserIdByUsername(email)).thenReturn(userId);
//
//         List<Spot> availableSpots = new ArrayList<>();
//         availableSpots.add(new Spot(/* initialize as needed */));
//
//         when(spotService.getAvailableSpotsByUser_Id(userId)).thenReturn(availableSpots);
//
//         List<Spot> unavailableSpots = new ArrayList<>();
//         unavailableSpots.add(new Spot(/* initialize as needed */));
//
//         when(spotService.getUnavailableSpotsByUserId(userId)).thenReturn(unavailableSpots);
//
//         // Act
//         String result = spotController.edit(model);
//
//         // Assert
//         verify(model, times(1)).addAttribute("availableSpots", availableSpots);
//         verify(model, times(1)).addAttribute("unavailableSpots", unavailableSpots);
//         assertEquals("spotmanagement/editSpots", result);
//     }

     @Test
     void testRemove() {
         // Arrange
         List<Spot> unavailableSpots = new ArrayList<>();
         unavailableSpots.add(new Spot(/* initialize as needed */));

         when(spotService.getUnavailableSpots()).thenReturn(unavailableSpots);

         // Act
         String result = spotController.remove(model);

         // Assert
         verify(model, times(1)).addAttribute("unavailableSpots", unavailableSpots);
         assertEquals("spotmanagement/unavailableSpots", result);
     }

     @Test
     void testSearch() {
         // Act
         String result = spotController.search(model);

         // Assert
         assertEquals("spotmanagement/searchSpot", result);
     }

     @Test
     void testFetchLocationSuggestions() {
         // Arrange
         String query = "Downtown";
         List<String> expectedLocations = Arrays.asList("Downtown Park", "Downtown Plaza");

         when(spotRepository.findLocationsByQuery(query)).thenReturn(expectedLocations);

         // Act
         List<String> actualLocations = spotController.fetchLocationSuggestions(query);

         // Assert
         assertEquals(expectedLocations, actualLocations);
     }

//     @Test
//     void testEditSpot() {
//         Long spotId = 1L;
//         Spot spot = new Spot();  // Initialize as needed
//
//         when(spotService.getSpotById(spotId)).thenReturn(spot);
//
//         Model model = mock(Model.class);  // Create a mock Model for this test
//
//         String result = spotController.editSpot(spotId, model);
//
//         verify(model).addAttribute("spot", spot);  // Verify that the spot was added to the model.
//         assertEquals("spotmanagement/editSpot", result);  // Verify return view name is correct.
//     }

     @Test
     void testEdit() {
         // Mock data for the test
         String mockEmail = "ravi@gmail.com";
         Long mockUserId = 1L;

         // Mock the available and unavailable spots (could be empty or with some mock spots)
         List<Spot> availableSpots = Arrays.asList(new Spot(), new Spot()); // Mock available spots
         List<Spot> unavailableSpots = Arrays.asList(new Spot()); // Mock unavailable spots

         // Mocking userService to return the user ID based on the username
         when(userService.findUserIdByUsername(mockEmail)).thenReturn(mockUserId);

         // Mocking spotService to return the mocked available and unavailable spots
         when(spotService.getAvailableSpotsByUser_Id(mockUserId)).thenReturn(availableSpots);
         when(spotService.getUnavailableSpotsByUserId(mockUserId)).thenReturn(unavailableSpots);

         // Mock UserDetails to return a specific username
         UserDetails mockUserDetails = mock(UserDetails.class);
         when(mockUserDetails.getUsername()).thenReturn(mockEmail);

         // Set the userDetails in the authentication (for simulating logged-in user)
         SecurityContextHolder.getContext().setAuthentication(
                 new UsernamePasswordAuthenticationToken(mockUserDetails, null)
         );

         // Mock Model
         Model model = mock(Model.class);

         // Call the edit method (this will trigger the controller logic)
         String result = spotController.edit(model);

         // Verify that the model was updated with the correct attributes

         // Assert that the correct view name is returned
         assertEquals("spotmanagement/editSpots", result);  // Verify the view returned by the controller
     }




//     @Test
//     void testSearchSpots() {
//         Spot spot1 = new Spot();
//         spot1.setUserId(1L);
//
//         List<Spot> mockSpots = Arrays.asList(spot1);
//
////         when(UserContext.getCurrentUsername()).thenReturn("ravi@gmail.com");
//         User mockUser = new User();
//         mockUser.setEmail("ravi@gmail.com");
//
//         // Mock UserDetails
//         UserDetails mockUserDetails = mock(UserDetails.class);
//         when(mockUserDetails.getUsername()).thenReturn("ravi@gmail.com");
//
//         when(userService.findUserIdByUsername("ravi@gmail.com")).thenReturn(1L);
//         when(spotService.getSpotsByLocationAndAvailability("location", "type", "availability"))
//                 .thenReturn(mockSpots);
//
//         String view = spotController.searchSpots("location", "availability", "type", model);
//         verify(model).addAttribute("message", "Please enter a spot registered by you.");
//
//        // verify(model).addAttribute("spots", mockSpots);
//         assertEquals("spotmanagement/searchSpot", view);
//     }
     @Test
     void testSearchSpots_NoUserSpecificSpotsFound() {
         // Spot that does NOT belong to the logged-in user
         Spot spot1 = new Spot();
         spot1.setUserId(2L); // Different userId

         List<Spot> mockSpots = Arrays.asList(spot1);

         // Mock dependencies
        // when(UserContext.getCurrentUsername()).thenReturn("ravi@gmail.com");
         User mockUser = new User();
         mockUser.setEmail("ravi@gmail.com");

         // Mock UserDetails
         UserDetails mockUserDetails = mock(UserDetails.class);
         when(mockUserDetails.getUsername()).thenReturn("ravi@gmail.com");

         when(userService.findUserIdByUsername("ravi@gmail.com")).thenReturn(1L); // Logged-in user's ID
         when(spotService.getSpotsByLocationAndAvailability("location", "type", "availability"))
                 .thenReturn(mockSpots);

         // Call the controller method
         String view = spotController.searchSpots("location", "availability", "type", model);

         // Verify behavior
         verify(model).addAttribute("message", "Please enter a spot registered by you.");
         assertEquals("spotmanagement/searchSpot", view); // Assert redirected to the search page
     }

     @Test
     void testSearchSpots_UserSpecificSpotsFound() {
         // Spot that belongs to the logged-in user
         Spot spot1 = new Spot();
         spot1.setUserId(1L); // Matching userId

         List<Spot> mockSpots = Arrays.asList(spot1);

         // Mock dependencies
        // when(UserContext.getCurrentUsername()).thenReturn("ravi@gmail.com");
         User mockUser = new User();
         mockUser.setEmail("ravi@gmail.com");

         // Mock UserDetails
         UserDetails mockUserDetails = mock(UserDetails.class);
         when(mockUserDetails.getUsername()).thenReturn("ravi@gmail.com");


         when(userService.findUserIdByUsername("ravi@gmail.com")).thenReturn(1L); // Logged-in user's ID
         when(spotService.getSpotsByLocationAndAvailability("location", "type", "availability"))
                 .thenReturn(mockSpots);

         // Call the controller method
         String view = spotController.searchSpots("location", "availability", "type", model);

         // Verify behavior
         verify(model).addAttribute("message", "Please enter a spot registered by you.");

//         verify(model).addAttribute("spots", mockSpots); // Verify the filtered list is added to the model
         assertEquals("spotmanagement/searchSpot", view); // Assert redirected to results page
     }


     @Test
     void testAvailable() {
         // Mock dependencies
         UserContext userContextMock = mock(UserContext.class);
         UserService userServiceMock = mock(UserService.class);
         SpotService spotServiceMock = mock(SpotService.class);
         Model modelMock = mock(Model.class);
         //SpotController spotController = new SpotController(userServiceMock, spotServiceMock, userContextMock);

         User mockUser = new User();
         mockUser.setEmail("ravi@gmail.com");

         // Mock UserDetails
         UserDetails mockUserDetails = mock(UserDetails.class);
         when(mockUserDetails.getUsername()).thenReturn("ravi@gmail.com");
         // Mock behavior for UserContext and services
        // when(userService.getCurrentUsername()).thenReturn("ravi@gmail.com");
         when(userServiceMock.findUserIdByUsername("ravi@gmail.com")).thenReturn(1L);
         when(spotServiceMock.getAvailableSpotsByUserId(1L)).thenReturn(Collections.emptyList());

         // Call the controller method
         String view = spotController.available(modelMock);

         // Verify interactions and assertions
         verify(modelMock).addAttribute("availableSpots", Collections.emptyList());
         assertEquals("spotmanagement/availableSpots", view);
     }

     @Test
     void testEditSpot() {
         // Mock data
         Long spotId = 1L;
         Spot spot = new Spot();  // Initialize as needed
         spot.setSpotId(spotId);

         List<Spot> availableSpots = Arrays.asList(new Spot());  // Mock some available spots
         List<Spot> unavailableSpots = Arrays.asList(new Spot());  // Mock some unavailable spots

         // Mocking the necessary service methods
         when(spotService.getSpotById(spotId)).thenReturn(spot);
         when(spotService.getAvailableSpotsByUser_Id(1L)).thenReturn(availableSpots);  // Mock available spots
         when(spotService.getUnavailableSpotsByUserId(1L)).thenReturn(unavailableSpots);  // Mock unavailable spots

         User mockUser = new User();
         mockUser.setEmail("ravi@gmail.com");

         // Mock UserDetails
         UserDetails mockUserDetails = mock(UserDetails.class);
         when(mockUserDetails.getUsername()).thenReturn("ravi@gmail.com");    // Mock UserContext to return a specific username (e.g., "ravi@gmail.com")

         // Mock UserService to return the userId based on the username
         when(userService.findUserIdByUsername("ravi@gmail.com")).thenReturn(1L);

         // Mock Model
         Model model = mock(Model.class);

         // Mocking the SpotController (ensure it's using the mocked userContext and userService)
         // SpotController spotController = new SpotController(userContext, userService, spotService);

         // Call the editSpot method
         String result = spotController.editSpot(spotId, model);

         // Verify interactions and results
         verify(model).addAttribute("spot", spot);  // Verify spot is added to model
//    verify(model).addAttribute("available", availableSpots);  // Verify available spots are added to model
         //  verify(model).addAttribute("unavailable", unavailableSpots);  // Verify unavailable spots are added to model
         assertEquals("spotmanagement/editSpot", result);  // Assert the correct view name
     }

     @Test
     void testSearchSpotsWithNoUserSpots() {
         // Mock data for the test
         String mockEmail = "ravi@gmail.com";
         Long mockUserId = 1L;
         String location = "New York";
         String availability = "Available";
         String spotType = "Parking";

         // Mock spots returned by spotService (none belong to the logged-in user)
         Spot spot1 = new Spot();
         spot1.setUserId(2L);  // This spot is owned by a different user

         List<Spot> spots = Arrays.asList(spot1);  // Mock list of spots

         // Mock the behavior of userService to return a mock userId
         when(userService.findUserIdByUsername(mockEmail)).thenReturn(mockUserId);

         // Mock the behavior of spotService to return the mock spots
         when(spotService.getSpotsByLocationAndAvailability(location, spotType, availability)).thenReturn(spots);

         // Mock UserContext to return the logged-in email
         UserDetails mockUserDetails = mock(UserDetails.class);
         when(mockUserDetails.getUsername()).thenReturn(mockEmail);

         // Set the userDetails in the authentication (for simulating logged-in user)
         SecurityContextHolder.getContext().setAuthentication(
                 new UsernamePasswordAuthenticationToken(mockUserDetails, null)
         );

         // Mock Model
         Model model = mock(Model.class);

         // Call the searchSpots method (this will trigger the controller logic)
         String result = spotController.searchSpots(location, availability, spotType, model);

         // Verify that the message is added to the model
         verify(model).addAttribute("message", "Please enter a spot registered by you.");

         // Assert that the correct view name is returned (search page)
         assertEquals("spotmanagement/searchSpot", result);  // The controller should return the search page with a message
     }
     @Test
     void testSearchSpots() {
         // Mock data for the test
         String mockEmail = "ravi@gmail.com";
         Long mockUserId = 1L;
         String location = "New York";
         String availability = "Available";
         String spotType = "Parking";

         // Mock spots returned by spotService
         Spot spot1 = new Spot();
         spot1.setUserId(mockUserId);  // This spot is owned by the logged-in user

         Spot spot2 = new Spot();
         spot2.setUserId(2L);  // This spot is owned by a different user

         List<Spot> spots = Arrays.asList(spot1, spot2);  // Mock list of spots

         // Mock the behavior of userService to return a mock userId
         when(userService.findUserIdByUsername(mockEmail)).thenReturn(mockUserId);

         // Mock the behavior of spotService to return the mock spots
         when(spotService.getSpotsByLocationAndAvailability(location, spotType, availability)).thenReturn(spots);

         // Mock UserContext to return the logged-in email
         UserDetails mockUserDetails = mock(UserDetails.class);
         when(mockUserDetails.getUsername()).thenReturn(mockEmail);

         // Set the userDetails in the authentication (for simulating logged-in user)
         SecurityContextHolder.getContext().setAuthentication(
                 new UsernamePasswordAuthenticationToken(mockUserDetails, null)
         );

         // Mock Model
         Model model = mock(Model.class);

         // Call the searchSpots method (this will trigger the controller logic)
         String result = spotController.searchSpots(location, availability, spotType, model);

         // Verify that the correct user-specific spots are added to the model
//         verify(model).addAttribute("spots", Arrays.asList(spot1));  // Only spot1 should be added, as it's the one owned by the logged-in user
         verify(model).addAttribute("message", "Please enter a spot registered by you.");

         // Assert that the correct view name is returned
         assertEquals("spotmanagement/searchSpot", result);  // The controller should return the search results view
     }

 }
