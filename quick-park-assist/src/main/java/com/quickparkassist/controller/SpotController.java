package com.quickparkassist.controller;

import com.quickparkassist.model.Spot;
import com.quickparkassist.model.User;
import com.quickparkassist.repository.SpotRepository;
import com.quickparkassist.service.SpotService;

import java.util.List;
import java.util.stream.Collectors;

import com.quickparkassist.service.UserServiceImpl;
import com.quickparkassist.util.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SpotController {

    @Autowired
    private SpotService spotService;

    @Autowired
    private UserServiceImpl userService;

    private static final String USER = "user";
    private static final String AVAILBALESPOTS = "availableSpots";
    private static final String UNAVAILBALESPOTS = "unavailableSpots";
    private static final String MESSAGE = "message";
    private static final Logger logger = LoggerFactory.getLogger(SpotController.class);


    @GetMapping("/spot-management")
    public String home(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {
        if (loggedInUser != null) {
            logger.info("Logged-in user: {}", loggedInUser.getUsername());

            // Fetch logged-in user details using email
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute(USER, user); // Pass only this user to the view
        } else {
            logger.warn("No user is logged in.");

            model.addAttribute(USER, null); // No user is logged in
        }
        logger.info("Fetching available spots and unavailable spots.");

        model.addAttribute(AVAILBALESPOTS, spotService.getAvailableSpots());
        model.addAttribute(UNAVAILBALESPOTS, spotService.getUnavailableSpots());
        return "spotmanagement/addSpots";
    }

    @GetMapping("/add")
    public String adding() {
        logger.info("Navigating to add new spots page.");
        return "spotmanagement/addSpots";
    }

    @GetMapping("/available")
    public String available(Model model) {
        String email = UserContext.getCurrentUsername();
        logger.info("Fetching available spots for user: {}", email);

        Long userId = userService.findUserIdByUsername(email);
// Fetch the available spots owned by the logged-in user
        List<Spot> userSpots = spotService.getAvailableSpotsByUserId(userId);

        logger.info("User '{}' has {} available spots.", email, userSpots.size());

        model.addAttribute(AVAILBALESPOTS, userSpots);

        return "spotmanagement/availableSpots";
    }

    @GetMapping("/edit")
    public String edit(Model model) {
        // Get the currently logged-in user's email or username
        String email = UserContext.getCurrentUsername();
        logger.info("User '{}' is attempting to edit their spots.", email);
        // Fetch the user ID using the email/username
        Long userId = userService.findUserIdByUsername(email);
        logger.info("Fetched user ID for email '{}': {}", email, userId);

        // Get the spots for the logged-in user
        model.addAttribute(AVAILBALESPOTS, spotService.getAvailableSpotsByUser_Id(userId)); // Only available spots for the user
        model.addAttribute(UNAVAILBALESPOTS, spotService.getUnavailableSpotsByUserId(userId)); // Only unavailable spots for the user

        return "spotmanagement/editSpots";
    }

    @GetMapping("/remove")
    public String remove(Model model) {
        logger.info("Fetching all unavailable spots for removal.");

        String email = UserContext.getCurrentUsername();
        logger.info("User '{}' is attempting to edit their spots. ", email);
        // Fetch the user ID using the email/username
        Long userId = userService.findUserIdByUsername(email);
        logger.info("Fetched user ID for email  '{}': {}", email, userId);


        model.addAttribute(UNAVAILBALESPOTS, spotService.getUnavailableSpotsByUserId(userId)); // Only unavailable spots for the user

        return "spotmanagement/unavailableSpots";
    }
    @PostMapping("/addSpot")
    public String addSpot(@ModelAttribute Spot spot, RedirectAttributes redirectAttributes) {
        // Get the currently logged-in user's email/username
        String email = UserContext.getCurrentUsername();
        logger.info("User '{}' is attempting to add a new spot.", email);

        // Fetch the user ID using the email/username
        Long userId = userService.findUserIdByUsername(email);
        logger.info("Fetched user ID '{}' for the user '{}'.", userId, email);

        // Set the user ID to the spot
        spot.setUserId(userId);

        spotService.saveSpot(spot);
        logger.info("Spot for user '{}' added successfully with ID '{}'.", email, spot.getSpotId());

        redirectAttributes.addFlashAttribute(MESSAGE, "Spot added successfully!");

        return "redirect:/add";
    }

    @GetMapping("/search")
    public String search(Model model) {
        logger.info("Accessing the search page for spot management.");
        return "spotmanagement/searchSpot";
    }

    @PostMapping("/searchspots")
    public String searchSpots(@RequestParam String location,
                              @RequestParam String availability,
                              @RequestParam("spotType") String spotType,
                              Model model) {
        logger.info("Searching for spots with location: {}, availability: {}, spot type: {}", location, availability, spotType);

        String email = UserContext.getCurrentUsername();
        Long userId = userService.findUserIdByUsername(email);
// Fetch the spots based on location, spot type, and availability
        List<Spot> spots = spotService.getSpotsByLocationAndAvailability(location, spotType, availability);

        // Check if the spots belong to the logged-in user
        List<Spot> userSpots = spots.stream()
                .filter(spot -> spot.getUserId() != null && spot.getUserId().equals(userId))
                .collect(Collectors.toList());
        if (userSpots.isEmpty()) {
            logger.warn("No spots found for the current user with the provided filters.");

            // No spots found for the current user, add a message to the model
            model.addAttribute(MESSAGE, "Please enter a spot registered by you.");
            return "spotmanagement/searchSpot"; // Return to the search page with the message
        }

        // Add the user-specific spots to the model
        model.addAttribute("spots", userSpots);
        logger.info("Found {} spots for the current user", userSpots.size());

        return "spotmanagement/searchResults";
    }

    @Autowired
    private SpotRepository spotRepository;

    @GetMapping("/fetchLocationSuggestions")
    @ResponseBody
    public List<String> fetchLocationSuggestions(@RequestParam("query") String query) {
        // Assuming SpotRepository has a method to fetch location based on the query\
        logger.info("Fetching location suggestions for query: {}", query);

        return spotRepository.findLocationsByQuery(query);
    }

    @GetMapping("/editSpot/{spotId}")
    public String editSpot(@PathVariable Long spotId, Model model) {
        logger.info("Editing spot with ID: {}", spotId);
        Spot spot = spotService.getSpotById(spotId);
        model.addAttribute("spot", spot);
        return "spotmanagement/editSpot";
    }

    @PostMapping("/modifySpot")
    public String modifySpot(@ModelAttribute Spot spot) {
        logger.info("Modifying spot with ID: {}", spot.getSpotId());

        System.out.println("modify spot controller is called");
        String email = UserContext.getCurrentUsername();
        Long userId = userService.findUserIdByUsername(email);

        spot.setUserId(userId);
        spotService.updateSpot(spot);
        return "redirect:/edit";
    }

    @PostMapping("/removeSpot")
    public String removeSpot(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        logger.info("Removing spot with ID: {}", id);
        spotService.removeSpot(id);
        redirectAttributes.addFlashAttribute(MESSAGE, "Spot removed successfully!");
        return "redirect:/remove";
    }

}
