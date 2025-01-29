package com.quickparkassist.controller;


import com.quickparkassist.model.*;
import com.quickparkassist.repository.SpotRepository;
import com.quickparkassist.repository.VehicleRepository;
import com.quickparkassist.service.*;
import com.quickparkassist.util.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reservations")
public class EvController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private SpotRepository spotRepository;

    @Autowired
    private SpotService spotService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private EvService evService;


    @Autowired
    private EVChargingStationService chargingStationService;

    private static final String VEHICLES = "vehicles";
    private static final String USER = "user";
    private static final String RESERVATIONS = "reservations";
    private static final String RESERVATION = "reservation";
    private static final Logger logger = LoggerFactory.getLogger(EvController.class);

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        logger.info("Initializing binder with custom date format.");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @GetMapping({"/create", "", "/"})
    public String createReservationForm(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {
        logger.info("Entering the reservation creation form.");

        if (loggedInUser != null) {
            logger.info("Logged-in user: {}", loggedInUser.getUsername());

            // Fetch logged-in user details using email
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute(USER, user); // Pass the logged-in user to the view

            // Fetch vehicles for the current user where hasElectric == "YES"
            List<Vehicle> electricVehicles = vehicleRepository.findByUserEmailAndHasElectric(user.getEmail(), "YES");
            // Add electric vehicles to the model
            model.addAttribute(VEHICLES, electricVehicles);
            logger.info("Fetched electric vehicles for user: {}", electricVehicles.size());

        } else {
            model.addAttribute(USER, null); // No user is logged in
            model.addAttribute(VEHICLES, Collections.emptyList()); // Empty vehicle list
            logger.warn("No user logged in. Empty vehicle list.");
        }

        // Add a new reservation model attribute
        model.addAttribute(RESERVATION, new Evmodel());

        // Fetch all parking spots where spot_type is 'ev'
        List<Spot> evSpots = spotService.getSpotsByType("ev");
        model.addAttribute("evSpots", evSpots);
        logger.info("Fetched EV spots for the reservation form.");

        return "reservations/create";
    }



    @GetMapping("/view")
    public String viewAllReservations(Model model) {
        logger.info("Fetching all reservations for the logged-in user.");

        // Get the email (username) of the current user from UserContext
        String email = UserContext.getCurrentUsername();

        // Fetch the userId using the email
        Long userId = userService.findUserIdByUsername(email);

        if (userId == null) {
            logger.warn("User ID is null for email: {}", email);

            // Handle the case where userId is null, maybe show an error page or return an empty list
            model.addAttribute(RESERVATIONS, new ArrayList<>()); // No reservations available
            return "reservations/ "; // Or redirect to an error page
        }


        // Get all reservations and filter them by userId
        List<Evmodel> reservations = evService.getAllReservations().stream()
                .filter(reservation -> reservation.getUserId().equals(userId)) // Only include reservations for the logged-in user
                .collect(Collectors.toList());

        model.addAttribute(RESERVATIONS, reservations);
        logger.info("Fetched {} reservations for userId: {}", reservations.size(), userId);

        return "reservations/view";
    }

    @GetMapping("/edit_ev")
    public String edit_ev(Model model) {
        logger.info("Entering edit reservations page for the logged-in user.");

        // Get the email (username) of the current user from UserContext
        String email = UserContext.getCurrentUsername();

        // Fetch the userId using the email
        Long userId = userService.findUserIdByUsername(email);

        // Get all reservations and filter them by userId
        List<Evmodel> reservations = evService.getAllReservations().stream()
                .filter(reservation -> reservation.getUserId().equals(userId)) // Only include reservations for the logged-in user
                .collect(Collectors.toList());
        model.addAttribute(RESERVATIONS, reservations);
        logger.info("Fetched {} reservations for editing by userId: {}", reservations.size(), userId);

        return "reservations/edit_page";
    }

    @GetMapping("/delete_ev")
    public String delete_ev(Model model) {
        logger.info("Entering delete reservations page for the logged-in user.");

        // Get the email (username) of the current user from UserContext
        String email = UserContext.getCurrentUsername();

        // Fetch the userId using the email
        Long userId = userService.findUserIdByUsername(email);

        // Get all reservations and filter them by userId
        List<Evmodel> reservations = evService.getAllReservations().stream()
                .filter(reservation -> reservation.getUserId().equals(userId)) // Only include reservations for the logged-in user
                .collect(Collectors.toList());
        model.addAttribute(RESERVATIONS, reservations);
        logger.info("Fetched {} reservations for deletion by userId: {}", reservations.size(), userId);

        return "reservations/delete_page";
    }



    @PostMapping("/save")
    public String saveReservation(@ModelAttribute(RESERVATION) Evmodel reservation, RedirectAttributes redirectAttributes) {
        // Get the email (username) of the current user from UserContext
        logger.info("Attempting to save reservation for user: {}", reservation.getUserId());

        String email = UserContext.getCurrentUsername();

        // Fetch the userId using the email
        Long userId = userService.findUserIdByUsername(email);

        // Set the userId on the reservation model
        reservation.setUserId(userId);
        // Assuming start_time is a LocalTime object and you have a setter for it
        LocalTime startTime = LocalTime.parse(reservation.getStart_time());

        // Calculate the end_time based on duration
        LocalTime endTime = reservation.calculateEndTime(startTime);


        reservation.setEnd_time(String.valueOf(endTime)); // If you still need to store it
        Long spotId = reservation.getEvSpot();
        Spot spot = spotService.getSpotById(spotId);

        if (spot != null) {
            // Set the spot details (name and location) on the reservation
            reservation.setSpotName(spot.getSpotName());
            reservation.setLocation(spot.getLocation());
            logger.info("Assigned spot {} to reservation.", spot.getSpotName());

        } else {
            // Handle the case when the spot is not found, for example, add an error message
            redirectAttributes.addFlashAttribute("errorMessage", "Selected EV spot not found.");
            logger.error("EV spot not found with ID: {}", spotId);

            return "redirect:/reservations";
        }

        evService.saveReservation(reservation);

        redirectAttributes.addFlashAttribute("message", "Your charging slot has been successfully reserved.");
        logger.info("Reservation saved successfully for userId: {}", userId);

        return "redirect:/reservations";
    }

    @GetMapping("/edit/{id}")
    public String editReservationForm(@PathVariable Long id, Model model,
                                      @AuthenticationPrincipal UserDetails loggedInUser) {
        logger.info("Editing reservation with ID: {}", id);
        Evmodel reservation = evService.getReservationById(id);

        model.addAttribute("reservation", reservation);

        // Check if a user is logged in
        if (loggedInUser != null) {
            // Fetch logged-in user details using email
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute("user", user); // Pass the logged-in user to the view

            // Fetch vehicles for the current user where hasElectric == "YES"
            List<Vehicle> electricVehicles = vehicleRepository.findByUserEmailAndHasElectric(user.getEmail(), "YES");
            model.addAttribute("vehicles", electricVehicles); // Add electric vehicles to the model
        } else {
            model.addAttribute("user", null); // No user is logged in
            model.addAttribute("vehicles", Collections.emptyList()); // Empty vehicle list
        }

        // Fetch all parking spots where spot_type is 'ev'
        List<Spot> evSpots = spotService.getSpotsByType("ev");
        model.addAttribute("evSpots", evSpots);

        return "reservations/update";
    }

    @PostMapping("/update/{id}")
    public String updateReservation(@ModelAttribute(RESERVATION) Evmodel reservation) {
        logger.info("Updating reservation with ID: {}", reservation.getId());

        // Get the email (username) of the current user from UserContext
        String email = UserContext.getCurrentUsername();

        // Fetch the userId using the email
        Long userId = userService.findUserIdByUsername(email);

        // Check if the userId is not null before setting it
        if (userId != null) {
            reservation.setUserId(userId); // Set the userId in the reservation object
        } else {
            logger.error("User ID is null for email: {}", email);
            // Handle the case where userId is null (perhaps redirect to login or error page)
            return "redirect:/"; // Example: redirect to login page if user is not found
        }
        // Fetch the evSpot using the spotId and set it in the reservation object
        Long spotId = reservation.getEvSpot();  // Assuming evSpot is the spotId
        Spot spot = spotService.getSpotById(spotId);  // Fetch the spot object by id
        if (spot != null) {
            reservation.setSpotName(spot.getSpotName()); // Set the spotName in the reservation
        }

// Parse the start time as LocalTime (only time, no date)
        LocalTime startTime = LocalTime.parse(reservation.getStart_time());  // This will handle "17:30"

// Calculate the end time based on the start time (assuming the method requires LocalTime)
        LocalTime endTime = reservation.calculateEndTime(startTime);

// Set the calculated end time (time only, no date involved)
        reservation.setEnd_time(endTime.toString());  // Store only the time part (e.g., "21:20")


        evService.saveReservation(reservation);
        logger.info("Reservation updated successfully with ID: {}", reservation.getId());

        return "redirect:/reservations/edit_ev";
    }

    @GetMapping("/delete/{id}")
    public String deleteReservation(@PathVariable Long id) {
        logger.info("Attempting to delete reservation with ID: {}", id);

        evService.deleteReservation(id);
        logger.info("Reservation with ID: {} has been deleted.", id);

        return "redirect:/reservations/delete_ev";
    }
}
