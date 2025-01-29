package com.quickparkassist.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.quickparkassist.model.User;
import com.quickparkassist.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quickparkassist.model.AddonService;
import com.quickparkassist.model.AddonServiceBooking;
import com.quickparkassist.repository.addonRepository;
import com.quickparkassist.repository.addonServiceBookingRepository;

import lombok.Getter;
import lombok.Setter;

@Controller
//@RestController
public class AddonController {
    @Autowired
    private addonRepository eRepo;
    @Autowired
    private addonServiceBookingRepository bookingRepo;
    @Autowired
    private UserServiceImpl userService;

    private static final String MESSAGE = "message";
    private static final String USER = "user";
    public static final String SERVICE = "service";
    public static final String ERROR  = "error";
    public static final String SERVICE_NOT_FOUND_MESSAGE = "Service not found.";
    private static final String REDIRECT_REMOVE_ADDON_SERVICES = "redirect:/removeAddonServices";
    private static final String REDIRECT_MODIFY_ADDON_SERVICES = "redirect:/modifySelectedAddonServices";
    private static final Logger logger = LoggerFactory.getLogger(AddonController.class);

    private static final String USER_LOG_MESSAGE = "User logged in with username: %s";
    private static final String NO_USER_LOG_MESSAGE = "No user is logged in";

    @GetMapping({ "/addAddonServices" })
    public ModelAndView addAddonServices(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {
        logger.info("Accessing /addAddonServices endpoint");
        if (loggedInUser != null) {
            // Fetch logged-in user details using email
            logger.info(String.format(USER_LOG_MESSAGE, loggedInUser.getUsername()));
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute(USER, user); // Pass only this user to the view
        } else {
            logger.warn(NO_USER_LOG_MESSAGE);

            model.addAttribute(USER, null); // No user is logged in
        }
        ModelAndView mav = new ModelAndView("addonService/index");
        AddonService newAddon = new AddonService();
        mav.addObject("addon", newAddon);
        logger.info("Returning ModelAndView with addon object");
        return mav;
    }

    @PostMapping({ "/saveAddonServices" })
    public String createAddonServices(@ModelAttribute AddonService newAddon,  RedirectAttributes redirectAttributes) {
        logger.info("Attempting to save a new addon service: {}", newAddon);
        eRepo.save(newAddon);
        logger.info("Addon service saved successfully: {}", newAddon);
        // Add success message
        redirectAttributes.addFlashAttribute(MESSAGE, "Addon service added successfully!");

        logger.info("Redirecting to /addAddonServices");
        return "redirect:/addAddonServices";
    }

    @GetMapping({ "/viewAllAddonServices" })
    public ModelAndView viewAllAddonServices(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {
        logger.info("Accessing /viewAllAddonServices endpoint");
        if (loggedInUser != null) {
            logger.info(String.format(USER_LOG_MESSAGE, loggedInUser.getUsername()));

            // Fetch logged-in user details using email
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute(USER, user); // Pass only this user to the view
        } else {
            logger.warn(NO_USER_LOG_MESSAGE);
            model.addAttribute(USER, null); // No user is logged in
        }
        ModelAndView mav = new ModelAndView("addonService/view-all-addon-services");
        List<AddonService> list = eRepo.findAll();
        logger.info("Fetched list of all addon services: {}", list.size());

        mav.addObject("addon", list);
        return mav;
    }

    @Getter
    @Setter
    public static class AddonRequest {
        private Long addonID;
        private Float duration;
    }

    @ResponseBody
    @PostMapping("/selectedAddonService")
    public ResponseEntity<String> processSelectedAddons(
            @RequestParam String serviceIDs,
            @RequestParam String durations) {

        logger.info("Received request to book addon services. Service IDs: {}, Durations: {}", serviceIDs, durations);

        try {
            AddonServiceBooking newBooking = AddonServiceBooking.builder()
                    .serviceIDs(serviceIDs)
                    .quantities(durations)
                    .build();
            bookingRepo.save(newBooking);
            logger.info("Successfully booked addon services. Service IDs: {}, Durations: {}", serviceIDs, durations);

        } catch (Exception e) {
            logger.error("Error processing the addon service booking request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing the request: " + e.getMessage());
        }

        return ResponseEntity.ok("Addon services booked successfully.");
    }

    @GetMapping("/modifySelectedAddonServices")
    public ModelAndView modifyView(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {

        logger.info("Accessing modifySelectedAddonServices endpoint");

        if (loggedInUser != null) {
            logger.info(String.format(USER_LOG_MESSAGE, loggedInUser.getUsername()));

            // Fetch logged-in user details using email
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute(USER, user); // Pass only this user to the view


        }

        else {
            model.addAttribute(USER, null); // No user is logged in

        }
        ModelAndView mav = new ModelAndView("addonService/modify-addon-services");

        List<AddonServiceBooking> bookingList = bookingRepo.findAll();
        List<AddonService> allServices = eRepo.findAll();
        logger.info("Fetched {} bookings and {} addon services from the repository", bookingList.size(), allServices.size());

        // Convert list of all services into a Map for quick lookup
        Map<Long, AddonService> serviceMap = allServices.stream()
                .collect(Collectors.toMap(AddonService::getServiceID, service -> service));

        logger.info("Converted all services into a map for quick lookup");
        // Prepare a structure to hold filtered services for each booking
        List<Map<String, Object>> addonBookingData = new ArrayList<>();

        for (AddonServiceBooking booking : bookingList) {
            String[] serviceIDsArray = booking.getServiceIDs().split(",");
            String[] quantitiesArray = booking.getQuantities().split(",");

            List<Map<String, Object>> servicesWithQuantities = new ArrayList<>();

            for (int i = 0; i < serviceIDsArray.length; i++) {
                String serviceIDStr = serviceIDsArray[i].trim();
                if (!serviceIDStr.isEmpty()) {
                    Long serviceID = Long.parseLong(serviceIDsArray[i].trim());

                    double quantity = Double.parseDouble(quantitiesArray[i].trim()); // Works for "0.0"

                    // Fetch service details if it exists
                    if (serviceMap.containsKey(serviceID)) {
                        Map<String, Object> serviceData = new HashMap<>();
                        serviceData.put(SERVICE, serviceMap.get(serviceID));
                        serviceData.put("quantity", quantity);
                        servicesWithQuantities.add(serviceData);
                    }
                }
            }

            // Store each booking's data along with its filtered services
            Map<String, Object> bookingData = new HashMap<>();
            bookingData.put("booking", booking);
            bookingData.put("services", servicesWithQuantities);

            addonBookingData.add(bookingData);
        }
        logger.info("Processed {} addon bookings with their filtered services", addonBookingData.size());

        mav.addObject("addonBookingData", addonBookingData);
        return mav;
    }

    @GetMapping("/modifyAddonService")
    public ModelAndView modifyAddonService(@RequestParam Long serviceID) {
        ModelAndView mav = new ModelAndView("addonService/modify-addon-price");

        logger.info("Accessing modifyAddonService endpoint with serviceID: {}", serviceID);

        Optional<AddonService> optionalService = eRepo.findById(serviceID);
        if (optionalService.isPresent()) {
            logger.info("AddonService found with serviceID: {}", serviceID);
            mav.addObject(SERVICE, optionalService.get());
        } else {
            logger.warn("No AddonService found with serviceID: {}", serviceID);
            mav.addObject(ERROR, SERVICE_NOT_FOUND_MESSAGE);
        }

        return mav;
    }

    @PostMapping("/updateAddonServicePrice")
    public String updateAddonServicePrice(@RequestParam Long serviceID,
                                          @RequestParam float newPrice,
                                          RedirectAttributes redirectAttributes) {

        logger.info("Updating price for AddonService with serviceID: {}", serviceID);

        Optional<AddonService> optionalService = eRepo.findById(serviceID);

        if (optionalService.isPresent()) {
            AddonService service = optionalService.get();
            logger.info("Found AddonService with serviceID: {}. Updating price to {}", serviceID, newPrice);
            service.setPrice((float)newPrice);
            eRepo.save(service);
            redirectAttributes.addFlashAttribute("success", "Price updated successfully.");
        } else {
            logger.warn("No AddonService found with serviceID: {}", serviceID);
            redirectAttributes.addFlashAttribute(ERROR, SERVICE_NOT_FOUND_MESSAGE);
        }

        return REDIRECT_REMOVE_ADDON_SERVICES;
    }

    @GetMapping("/modifyAddonServiceDuration")
    public ModelAndView showModifyDurationPage(@RequestParam Long serviceID) {
        logger.info("Accessing modifyAddonServiceDuration page with serviceID: {}", serviceID);

        ModelAndView mav = new ModelAndView("addonService/modify-addon-service-duration");

        Optional<AddonService> optionalService = eRepo.findById(serviceID);
        if (optionalService.isPresent()) {
            logger.info("AddonService found with serviceID: {}", serviceID);

            mav.addObject(SERVICE, optionalService.get());
        } else {
            logger.warn("No AddonService found with serviceID: {}", serviceID);
            mav.addObject(ERROR, SERVICE_NOT_FOUND_MESSAGE);
        }

        return mav;
    }

    @PostMapping("/updateAddonServiceDuration")
    public String updateAddonServiceDuration(@RequestParam Long serviceID,
                                             @RequestParam float newDuration,
                                             RedirectAttributes redirectAttributes) {

        logger.info("Attempting to update duration for AddonService with serviceID: {}", serviceID);

        Optional<AddonService> optionalService = eRepo.findById(serviceID);

        if (optionalService.isPresent()) {
            AddonService service = optionalService.get();
            logger.info("AddonService found with serviceID: {}. Updating duration to {}", serviceID, newDuration);

            service.setDuration(newDuration); // Ensure setDuration uses Double
            eRepo.save(service);
            redirectAttributes.addFlashAttribute("success", "Duration updated successfully.");
        } else {
            logger.warn("No AddonService found with serviceID: {}", serviceID);

            redirectAttributes.addFlashAttribute(ERROR, SERVICE_NOT_FOUND_MESSAGE);
        }

        return REDIRECT_REMOVE_ADDON_SERVICES;
    }

    // Delete an individual service from a booking
    @PostMapping("/deleteService")
    public String deleteService(@RequestParam Long bookingID, @RequestParam Long serviceID, RedirectAttributes redirectAttributes) {
        logger.info("Attempting to delete service with serviceID: {} from booking with bookingID: {}", serviceID, bookingID);

        Optional<AddonServiceBooking> optionalBooking = bookingRepo.findById(bookingID);

        if (optionalBooking.isPresent()) {
            AddonServiceBooking booking = optionalBooking.get();
            String[] serviceIDsArray = booking.getServiceIDs().split(",");
            String[] quantitiesArray = booking.getQuantities().split(",");

            List<String> newServiceIDs = new ArrayList<>();
            List<String> newQuantities = new ArrayList<>();

            for (int i = 0; i < serviceIDsArray.length; i++) {
                if (!serviceIDsArray[i].trim().equals(serviceID.toString())) {
                    newServiceIDs.add(serviceIDsArray[i]);
                    newQuantities.add(quantitiesArray[i]);
                }
            }

            // Update the booking details
            booking.setServiceIDs(String.join(",", newServiceIDs));
            booking.setQuantities(String.join(",", newQuantities));

            bookingRepo.save(booking);
            redirectAttributes.addFlashAttribute(MESSAGE, "Service deleted successfully!");
        }

        return REDIRECT_MODIFY_ADDON_SERVICES;
    }

    // Delete an entire booking
    @PostMapping("/deleteBooking")
    public String deleteBooking(@RequestParam Long bookingID, RedirectAttributes redirectAttributes) {
        logger.info("Attempting to delete booking with bookingID: {}", bookingID);

        if (bookingRepo.existsById(bookingID)) {
            bookingRepo.deleteById(bookingID);
            logger.info("Booking with bookingID: {} deleted successfully.", bookingID);

            redirectAttributes.addFlashAttribute(MESSAGE, "Booking deleted successfully!");
        } else {
            logger.warn("Booking with bookingID: {} not found.", bookingID);

            redirectAttributes.addFlashAttribute(ERROR, "Booking not found!");
        }

        return REDIRECT_MODIFY_ADDON_SERVICES;
    }

    @GetMapping("/removeAddonServices")
    public ModelAndView removeAddonServices(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {
        logger.info("Attempting to remove addon services");

        if (loggedInUser != null) {
            logger.info("User logged in with username: {}", loggedInUser.getUsername());
            // Fetch logged-in user details using email
            User user = userService.getUserByEmail(loggedInUser.getUsername());
            model.addAttribute(USER, user); // Pass only this user to the view
        } else {
            model.addAttribute(USER, null); // No user is logged in
            logger.warn("No user is logged in.");
        }
        ModelAndView mav = new ModelAndView("addonService/remove-addon-services");
        List<AddonService> list = eRepo.findAll();
        logger.info("Retrieved {} addon services from the repository", list.size());

        mav.addObject("addonServices", list);
        return mav;
    }

    @PostMapping("/deleteAddonService")
    public String deleteAddonService(@RequestParam Long serviceID, RedirectAttributes redirectAttributes) {
        logger.info("Attempting to delete addon service with serviceID: {}", serviceID);

        Optional<AddonService> service = eRepo.findById(serviceID);
        if (service.isPresent()) {
            eRepo.deleteById(serviceID);
            logger.info("Addon service with serviceID: {} deleted successfully", serviceID);

            redirectAttributes.addFlashAttribute(MESSAGE, "Addon service deleted successfully.");
        } else {
            logger.warn("Addon service with serviceID: {} not found", serviceID);

            redirectAttributes.addFlashAttribute("errorMessage", "Addon service not found.");
        }
        return "redirect:/removeAddonServices";
    }
}
