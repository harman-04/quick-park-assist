package com.quickparkassist.controller;


import com.quickparkassist.dto.VehicleDto;
import com.quickparkassist.model.Vehicle;
import com.quickparkassist.repository.VehicleRepository;
import com.quickparkassist.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.quickparkassist.model.User;
import com.quickparkassist.service.UserServiceImpl;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private VehicleRepository vehicleRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);


    public static final String VEHICLES = "vehicles";
    private static final String USER = "user";
    private static final String MESSAGE = "message";
    private static final String INDEX_PAGE="index";

    @GetMapping("/login")
    public String login() {
        logger.info("Accessing Login Page");

        return INDEX_PAGE;
    }

    @GetMapping("/register")
    public String register() {
        logger.info("Accessing the registration page");
        return "registersection/registeration";
    }

    @GetMapping("")
    public String home() {
        logger.info("Accessing the home page");
        return INDEX_PAGE;
    }

    @GetMapping("/delete")
    public String delete(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        String username = userDetails.getUsername();  // Get logged-in username
        logger.info("User with username '{}' is attempting to delete their account", username);

        User user = userService.findByUsername(username);  // Fetch user data
        model.addAttribute(USER, user);
        return "registersection/delete";
    }

    @GetMapping("/users")
    public String userProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();  // Get logged-in username
        logger.info("User '{}' is accessing their profile", username);

        User user = userService.findByUsername(username);  // Fetch user data

        if (user != null) {
            logger.info("User '{}' profile data loaded successfully", username);
            model.addAttribute(USER, user);
        } else {
            logger.warn("User '{}' profile not found", username);
        }

        //  model.addAttribute(USER, user);
        return "registersection/users";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        // Retrieve the logged-in user details using SecurityContextHolder
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();  // Get logged-in username
        logger.info("User '{}' is attempting to update their profile", username);

        User existingUser = userService.findUserById(user.getId()); // Fetch the existing user

        // Handle password update
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            logger.info("User '{}' password updated successfully", username);

        } else {
            user.setPassword(existingUser.getPassword()); // Preserve old password
            logger.info("User '{}' kept existing password", username);

        }

        // ✅ Preserve existing vehicles to avoid breaking foreign key constraints
        user.setVehicles(existingUser.getVehicles());

        userService.saveUser(user);
        logger.info("User '{}' profile updated and saved", username);

        // Success message
        redirectAttributes.addFlashAttribute(MESSAGE, "Profile updated successfully.");

        return "redirect:/users";
    }


    @GetMapping("/edit/{email}")
    public String editUserForm(@PathVariable String email, Model model) {
        logger.info("User is attempting to edit profile for email: {}", email);

        User user = userService.getUserByEmail(email);
        model.addAttribute(USER, user);
        return "registersection/edit";
    }

    @GetMapping("/remove/{email}")
    public String deleteUser(@PathVariable String email) {
        logger.info("User is attempting to remove account with email: {}", email);

        try {
            // Delete the user by email
            userService.deleteUserByEmail(email);
            logger.info("Successfully deleted user with email: {}", email);
        } catch (Exception e) {
            logger.error("Error occurred while trying to delete user with email: {}", email, e);
        }
//        userService.deleteUserByEmail(email);
//        logger.info("Successfully deleted user with email: {}", email);

        return "redirect:/logout";
    }




    @GetMapping("/")
    public String viewUserProfile(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {
        if (loggedInUser != null) {
            String username = loggedInUser.getUsername();
            logger.info("User '{}' is viewing their profile", username);
            // Fetch logged-in user details using email
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute(USER, user); // Pass only this user to the view
            logger.info("User profile loaded for '{}'", username);

        } else {
            logger.warn("No user logged in, redirecting to login page");

            model.addAttribute(USER, null); // No user is logged in
        }
        return INDEX_PAGE;// Adjust to the correct template name
    }

    @GetMapping("/add-vehicle")
    public String showAddVehiclePage() {
        logger.info("User is attempting to add a new vehicle");

        return "registersection/add-vehicle";  // Maps to add-vehicle.html
    }



    @PostMapping("/add-vehicle")
    public String addVehicle(@ModelAttribute("vehicle") VehicleDto vehicleDto, RedirectAttributes redirectAttributes) {
        logger.info("Attempting to add a new vehicle: {}", vehicleDto);

        vehicleService.save(vehicleDto);

        logger.info("Vehicle '{}' added successfully", vehicleDto.getVehicleModel());

        redirectAttributes.addFlashAttribute(MESSAGE, "Vehicle added successfully.");
        return "redirect:/registration";
    }
    //
// Delete vehicle
    @PostMapping("/vehicles/delete/{id}")
    public String deleteVehicle(@PathVariable Long id,  RedirectAttributes redirectAttributes) {
        logger.info("Attempting to delete vehicle with ID: {}", id);

        vehicleService.deleteVehicleById(id);
        logger.info("Vehicle with ID: {} deleted successfully.", id);

        // Get the currently logged-in user's email to fetch their updated vehicle list
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Fetch the updated list of vehicles for the logged-in user
        List<Vehicle> vehicles = vehicleRepository.findByUserEmail(email);

        logger.info("Fetched {} vehicles for user: {}", vehicles.size(), email);

        // Add the updated list of vehicles to the redirect attributes
        redirectAttributes.addFlashAttribute(VEHICLES, vehicles);
        redirectAttributes.addFlashAttribute(MESSAGE, "Vehicle deleted successfully.");
        return "redirect:/view-vehicle"; // Redirect back to the vehicle list
    }


    @GetMapping("/view-vehicle")
    public String getMyVehicles(Model model) {

        // Get the currently logged-in user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        logger.info("Fetching vehicle list for user: {}", email);

        // Fetch vehicles for the current user
        List<Vehicle> vehicles = vehicleRepository.findByUserEmail(email);
        logger.info("User '{}' has {} vehicles.", email, vehicles.size());
        // Add vehicles to the model
        model.addAttribute(VEHICLES, vehicles);

        return "registersection/view-vehicle";

    }

}
