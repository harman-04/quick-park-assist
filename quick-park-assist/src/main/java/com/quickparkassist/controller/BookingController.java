package com.quickparkassist.controller;


import com.quickparkassist.model.User;
import com.quickparkassist.model.Vehicle;
import com.quickparkassist.repository.BookingRepository;
import com.quickparkassist.repository.SpotRepository;
import com.quickparkassist.model.Spot;
import com.quickparkassist.repository.VehicleRepository;
import com.quickparkassist.service.SpotService;

import com.quickparkassist.model.Booking;
import com.quickparkassist.service.BookingService;

import com.quickparkassist.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



import com.quickparkassist.util.UserContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
//@RestController
//@RequestMapping("/api")
public class BookingController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private SpotRepository spotRepository;

    @Autowired
    private SpotService spotService;

    @Autowired
    private BookingService bookingService;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private BookingRepository bookingRepository;

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);


    private static final String PARKING_SPOTS = "parkingSpots";
    private static final String MESSAGE = "message";
    private static final String VEHICLES = "vehicles";
    private static final String USER = "user";
    private static final String BOOKINGS = "bookings";
    private static final String EDIT_BOOKING_VIEW = "booking/edit-booking";
    private static final String VIEW_BOOKING_BY_NUMBER = "booking/view-booking-by-number";
    private static final String VIEW_BOOKING_TO_CANCEL = "booking/cancel-booking";


    // Handle GET request for the booking page
    @GetMapping("/booking")
    public String mainBookingPage(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {
        logger.info("Accessing booking page for user: {}", loggedInUser != null ? loggedInUser.getUsername() : "Guest");

        if (loggedInUser != null) {
            // Fetch logged-in user details using email
            logger.debug("Fetching details for logged-in user: {}", loggedInUser.getUsername());

            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute(USER, user); // Pass only this user to the view
            logger.info("User details added to the model for user: {}", user.getEmail());

            // Fetch non-electric vehicles using user ID
            List<Vehicle> nonElectricVehicles = vehicleRepository.findByUserAndHasElectric(user, "NO");
            logger.info("Fetched {} non-electric vehicles for user: {}", nonElectricVehicles.size(), user.getEmail());

            model.addAttribute(VEHICLES, nonElectricVehicles);
        } else {
            logger.warn("No logged-in user found. Passing null values for user and vehicles.");

            model.addAttribute(USER, null); // No user is logged in
            model.addAttribute(VEHICLES, null);

        }
        // Fetch a list of available parking spots from the database
        List<Spot> availableParkingSpots = spotRepository.findByAvailability("YES");
        logger.info("Fetched {} available parking spots.", availableParkingSpots.size());
        model.addAttribute(PARKING_SPOTS, availableParkingSpots);

        logger.info("Returning booking page view.");

        return "booking/booking"; // Return the booking page
    }

    // Handle GET request for the book page
    @GetMapping("/book")
    public String booking(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {
        logger.info("Fetching booking page.");
        if (loggedInUser != null) {
            logger.debug("User logged in with username: {}", loggedInUser.getUsername());
            // Fetch logged-in user details using email
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute(USER, user); // Pass only this user to the view
            // Fetch non-electric vehicles using user ID
            List<Vehicle> nonElectricVehicles = vehicleRepository.findByUserAndHasElectric(user, "NO");

            model.addAttribute(VEHICLES, nonElectricVehicles);
        } else {
            logger.warn("No user is logged in.");
            model.addAttribute(USER, null); // No user is logged in
            model.addAttribute(VEHICLES, null);

        }
        // Fetch a list of available parking spots from the database
        List<Spot> availableParkingSpots =spotRepository.findByAvailability("YES");

        model.addAttribute(PARKING_SPOTS, availableParkingSpots);

        return "booking/booking"; // Return the booking page view
    }


    @GetMapping("/modify-Booking-Details")
    public String modifyBookingDetails(Model model) {
        logger.info("Fetching modify booking details page.");

        String email = UserContext.getCurrentUsername();

        // Fetch the user ID using the username
        Long userId = userService.findUserIdByUsername(email);

        bookingService.updateBookingStatuses();

        // Fetch bookings for the logged-in user with statuses "Confirmed" or "Pending"
        List<Booking> bookings = bookingService.findBookingsByUserIdAndStatuses(userId,  Arrays.asList(Booking.Status.CONFIRMED, Booking.Status.PENDING));

        // Place null startTime values at the end in descending order
        bookings.sort(Comparator.comparing(Booking::getStartTime, Comparator.nullsLast(Comparator.reverseOrder())));

        model.addAttribute(BOOKINGS, bookings);
        return "booking/modify-booking-details";  // Return the view where bookings will be displayed
    }

    @GetMapping("/view-Booking-Details")
    public String viewBookingDetails(Model model) {
        logger.info("Request to view booking details for the logged-in user.");

        // Get the current username
        String email = UserContext.getCurrentUsername();
        logger.debug("Fetched current username: {}", email);

        // Fetch the user ID using the username
        Long userId = userService.findUserIdByUsername(email);
        logger.info("User ID fetched for username {}: {}", email, userId);

        // Fetch bookings for the logged-in user with specific statuses
        List<Booking> bookings = bookingService.findBookingsByUserId(userId);
        logger.debug("Fetched {} bookings for user ID: {}", bookings.size(), userId);

        // Sort bookings by startTime in descending order (most recent first)
        bookings.sort((b1, b2) -> b2.getStartTime().compareTo(b1.getStartTime()));
        logger.info("Bookings sorted by start time in descending order for user ID: {}", userId);

        model.addAttribute(BOOKINGS, bookings);
        logger.info("Bookings added to the model for user ID: {}", userId);

        return "booking/view-booking";  // Return the view where bookings will be displayed
    }


    // Show the edit form with the current booking details
    @GetMapping("/editBooking/{id}")
    public String editBooking(@PathVariable("id") Long bookingId, Model model) {
        logger.info("Request to edit booking with ID: {}", bookingId);
        Booking booking = bookingService.findById(bookingId).orElse(null);

        if (booking == null) {
            // Handle booking not found (optional)
            logger.warn("Booking with ID: {} not found.", bookingId);

            model.addAttribute(MESSAGE, "Booking not found");
            return "booking/view-booking";
        }
        logger.info("Booking with ID: {} found. Preparing edit view.", bookingId);

        List<Spot> allParkingSpots = spotRepository.findAll();
        logger.debug("Fetched {} parking spots for booking ID: {}", allParkingSpots.size(), bookingId);

        List<Vehicle> vehicles = vehicleRepository.findAll(); // Fetch all vehicles for the dropdown
        logger.debug("Fetched {} vehicles for booking ID: {}", vehicles.size(), bookingId);


        model.addAttribute(PARKING_SPOTS, allParkingSpots); // Provide all parking spots
        model.addAttribute(VEHICLES, vehicles);
        model.addAttribute("booking", booking);
        logger.info("Added parking spots, vehicles, and booking details to the model for booking ID: {}", bookingId);

        return EDIT_BOOKING_VIEW;

    }



    @PostMapping("/updateBooking/{id}")
    public String updateBooking(@PathVariable("id") Long bookingId,
                                @ModelAttribute Booking booking,
                                @RequestParam String parkingSpotLocation,
                                @RequestParam Long vehicleId,
                                RedirectAttributes redirectAttributes,

                                Model model) {
        logger.debug("Updating booking with ID: {}", bookingId);

        try {

            Long spotId = Long.valueOf(parkingSpotLocation);
            Spot spot = spotRepository.findById(spotId).orElse(null);

            if (spot == null) {
                logger.error("Parking spot not found with ID: {}", spotId);
                model.addAttribute(MESSAGE, "Parking spot not found.");
                return EDIT_BOOKING_VIEW;
            }

            // Fetch the selected vehicle
            Vehicle vehicle = vehicleRepository.findById(vehicleId).orElse(null); // Fetch the vehicle by its ID

            if (vehicle == null) {
                logger.error("Vehicle not found with ID: {}", vehicleId);
                model.addAttribute(MESSAGE, "Vehicle not found.");
                return EDIT_BOOKING_VIEW; // Handle case when vehicle is not found
            }

            if (!bookingId.equals(booking.getId())) {
                logger.error("Invalid booking ID: expected {} but got {}", bookingId, booking.getId());

                model.addAttribute(MESSAGE, "Invalid booking ID.");

                return EDIT_BOOKING_VIEW;
            }

// Set the parking spot object in the booking
            booking.setSpot(spot);

            // Save the location of the selected parking spot in the booking
            booking.setLocation(spot.getLocation());  // Save the location instead of spotId
            booking.setVehicle(vehicle);

            // Save the updated Booking
            bookingService.saveBooking(booking);

            // Redirect to view the updated booking

            redirectAttributes.addFlashAttribute(MESSAGE, "Booking updated successfully");
            logger.info("Booking with ID: {} updated successfully", bookingId);
            return "redirect:/modify-Booking-Details";
        }
        catch (NumberFormatException e) {
            logger.error("Invalid parking spot location: {}", parkingSpotLocation, e);
            model.addAttribute(MESSAGE, "Invalid parking spot location.");
            return EDIT_BOOKING_VIEW;
        }
    }

    @GetMapping("/viewBookingByNumber")
    public String viewBookingPage() {
        logger.info("Navigating to the View Booking by Number page.");
        return VIEW_BOOKING_BY_NUMBER;  // user enter their mobile number and find bookings
    }

    //process the form and fetch bookings by mobile number
    @GetMapping("/viewBooking-number")
    public String viewBooking(@RequestParam("mobileNumber") String mobileNumber, Model model) {
        logger.info("Processing request to view bookings for mobile number: {}", mobileNumber);

        bookingService.updateBookingStatuses();
        logger.debug("Booking statuses updated successfully.");
        // Fetch the currently logged-in user's email/username
        String email = UserContext.getCurrentUsername();

        // Retrieve the user ID based on the username
        Long userId = userService.findUserIdByUsername(email);
        logger.info("Retrieved user ID for username {}: {}", email, userId);

        // Fetch the user's registered mobile number
        String registeredMobileNumber = userService.findMobileNumberByUserId(userId);
        logger.info("Retrieved registered mobile number for user ID {}: {}", userId, registeredMobileNumber);

        // ✅ Check if entered number matches the user's registered number
        // ✅ OR if the booking with that number belongs to the user
        boolean isOwnerOfBooking = bookingService.existsByMobileNumberAndUserId(mobileNumber, userId);
        logger.debug("Ownership check result for mobile number {} and user ID {}: {}", mobileNumber, userId, isOwnerOfBooking);

        if (!mobileNumber.equals(registeredMobileNumber) && !isOwnerOfBooking) {
            logger.warn("Mobile number {} is not registered for user ID {} and does not belong to any of their bookings.", mobileNumber, userId);

            model.addAttribute("error", "Please enter your own registered mobile number.");
            return "redirect:/viewBookingByNumber";
        }


        // Fetch bookings based on the mobile number
        List<Booking> bookings = bookingService.findByMobileNumber(mobileNumber);
        logger.info("Fetched {} bookings for mobile number: {}", bookings.size(), mobileNumber);

        // Sort bookings by startTime in descending order (most recent first)
        bookings.sort((b1, b2) -> b2.getStartTime().compareTo(b1.getStartTime()));
        logger.debug("Sorted bookings for mobile number {} by start time in descending order.", mobileNumber);

        model.addAttribute(BOOKINGS, bookings);  // Add bookings to the model
        logger.info("Added bookings to the model for mobile number: {}", mobileNumber);

        model.addAttribute(MESSAGE, "Here are your booking details for the entered mobile number.");

        // Return the same page (viewBookingForm.html) with the booking details
        return VIEW_BOOKING_BY_NUMBER;
    }

    @GetMapping("/viewBookingToCancel")
    public String viewBookings(Model model) {
        logger.info("Fetching bookings to cancel.");

        String email = UserContext.getCurrentUsername();

        // Fetch the user ID using the username
        Long userId = userService.findUserIdByUsername(email);

        List<Booking> bookings = bookingService.findBookingsByUserIdAndStatuses(userId,  Arrays.asList(Booking.Status.CONFIRMED, Booking.Status.PENDING));
        model.addAttribute(BOOKINGS, bookings);
        return "booking/cancel-booking";
    }

    @PostMapping("/cancelBooking/{id}")
    public String cancelBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        logger.info("Cancelling booking with ID: {}", id);
        bookingService.updateBookingStatusTOCancel(id, Booking.Status.CANCELLED);

        // Fetch the associated booking to access the parking spot
        Booking booking = bookingService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID"));

        Spot spot = booking.getSpot();  // Retrieve the parking spot from the booking

        // Validate that the spot is not null
        if (spot == null) {
            logger.error("No associated parking spot found for booking ID: {}", id);
            throw new IllegalStateException("No associated parking spot found for booking ID: " + id);
        }
        // Update the parking spot's availability to "YES"

        spot.setAvailability("yes");
        bookingRepository.save(booking);
        spotRepository.save(spot);
        redirectAttributes.addFlashAttribute(MESSAGE, "Booking canceled successfully");
        logger.info("Booking with ID: {} canceled successfully", id);

        return "redirect:/viewBookingToCancel"; // Redirect to refresh the bookings list
    }


    // Handle POST request for booking parking spot
    @PostMapping("/book")
    public String bookParkingSpot(
            @RequestParam String fullName,
            @RequestParam String parkingSpotId, // Use spotId to reference parking spots
            @RequestParam int duration,
            @RequestParam String startTime,
            @RequestParam String price,
            @RequestParam String paymentMethod,
            @RequestParam String mobileNumber,
//            @RequestParam String vehicleInfo,
            @RequestParam Long vehicleId,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        logger.info("Processing booking request for mobile number: {}", mobileNumber);

        try {

            String email = UserContext.getCurrentUsername();
            logger.debug("Fetched current user's email: {}", email);

            // Fetch the user ID using the username
            Long userId = userService.findUserIdByUsername(email);
            logger.debug("Retrieved user ID for email {}: {}", email, userId);

            // Convert startTime string to LocalDateTime
            LocalDateTime startDateTime = LocalDateTime.parse(startTime);
            logger.debug("Converted start time string '{}' to LocalDateTime: {}", startTime, startDateTime);

            // Convert parkingSpotId (String) to Long
            Long parkingSpotIdLong = Long.valueOf(parkingSpotId);
            logger.debug("Parsed parking spot ID '{}' to Long: {}", parkingSpotId, parkingSpotIdLong);

            // Find the selected parking spot by its spotId using Spot repository
            Spot spot = spotRepository.findById(parkingSpotIdLong)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid parking spot ID"));
            logger.info("Fetched parking spot details for ID {}: {}", parkingSpotIdLong, spot);

            // Fetch the selected vehicle
            Vehicle vehicle = vehicleRepository.findById(vehicleId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid vehicle ID"));
            logger.info("Fetched vehicle details for ID {}: {}", vehicleId, vehicle);

            // Calculate the price based on price per hour and duration
            double pricePerHour = spot.getPricePerHour();
            double totalPrice = pricePerHour * duration;
            logger.debug("Calculated total price: {} (Price per hour: {}, Duration: {})", totalPrice, pricePerHour, duration);

            // Create a new Booking object and set the values
            Booking booking = new Booking();
            booking.setFullName(fullName);
            booking.setMobileNumber(mobileNumber);
            booking.setSpot(spot); // Store the entire ParkingSpot object
            booking.setLocation(spot.getLocation()); // Set the parking spot location
            booking.setDuration(String.valueOf(duration)); // Convert int to String
            booking.setStartTime(startDateTime);
            booking.setPrice(String.format("%.2f", totalPrice)); // Store the price formatted as string
            booking.setPaymentMethod(paymentMethod);

            booking.setVehicle(vehicle);

            // Set the status to "CONFIRMED" by default
            booking.setStatus(Booking.Status.CONFIRMED);
            booking.setUserId(userId); // Use the  user ID
            // Save the booking to the database
            bookingService.saveBooking(booking);
            logger.info("Booking created successfully for user: {}", email);

            // Update parking spot availability to "NO"
            spot.setAvailability("NO");
            spotRepository.save(spot);
            logger.info("Updated parking spot availability to 'NO' for spot ID: {}", parkingSpotIdLong);

            // Add confirmation message to the model
            redirectAttributes.addFlashAttribute("confirmationMessage", "Booking Confirmed! Your spot is reserved.");

        } catch (Exception e) {
            logger.error("Error processing booking request: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to confirm booking: " + e.getMessage());

        }
        return "redirect:/booking";
    }
}
