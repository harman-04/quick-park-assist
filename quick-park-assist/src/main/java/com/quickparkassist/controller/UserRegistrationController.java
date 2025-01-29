package com.quickparkassist.controller;

import com.quickparkassist.model.User;
import com.quickparkassist.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.quickparkassist.dto.UserRegistrationDto;
import com.quickparkassist.dto.VehicleDto;
import com.quickparkassist.service.UserService;
import com.quickparkassist.service.VehicleService;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {


	private final UserService userService;


	@Autowired
	private UserServiceImpl userServiceImpl;

	private final VehicleService vehicleService;

	private static final String USER = "user";
	private static final Logger logger = LoggerFactory.getLogger(UserRegistrationController.class);


	public UserRegistrationController(UserService userService, VehicleService vehicleService) {
		this.userService = userService;
		this.vehicleService = vehicleService;
	}

	// User Registration DTO Binding
	@ModelAttribute(USER)
	public UserRegistrationDto userRegistrationDto() {
		logger.info("Creating a new UserRegistrationDto object for the registration form.");

		return new UserRegistrationDto();
	}

	// Vehicle DTO Binding
	@ModelAttribute("vehicle")
	public VehicleDto vehicleDto() {
		logger.info("Creating a new VehicleDto object for the vehicle registration form.");
		return new VehicleDto();
	}

	@GetMapping
	public String showRegistrationForm(Model model, @AuthenticationPrincipal UserDetails loggedInUser) {

		// Check if a user is logged in
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		boolean isLoggedIn = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
		logger.info("Checking login status: {}", isLoggedIn);
		// Pass the login status
		model.addAttribute("isLoggedIn", isLoggedIn);

		if (isLoggedIn && loggedInUser != null) {
			logger.info("Logged-in user: {}", loggedInUser.getUsername());
			// If logged in, fetch user details
			User user = userServiceImpl.getUserByEmail(loggedInUser.getUsername());
			model.addAttribute(USER, user);  // Pass user object to the view
		} else {
			logger.info("User not logged in, providing new UserRegistrationDto.");
			// If not logged in, provide a new UserRegistration for the form
			model.addAttribute(USER, new UserRegistrationDto());
		}

		return "registersection/registration";
	}

	// User Registration Submission
	@PostMapping
	public String registerUserAccount(@ModelAttribute(USER) UserRegistrationDto registrationDto) {
		logger.info("Attempting to register a new user with email: {}", registrationDto.getEmail());


		if (userService.emailExists(registrationDto.getEmail())) {
			logger.warn("Email already exists: {}", registrationDto.getEmail());

			return "redirect:registration?error";
		}
		userService.save(registrationDto);
		logger.info("User successfully registered with email: {}", registrationDto.getEmail());

		return "redirect:/registration?success";
	}


}
