package com.quickparkassist.controller;

import com.quickparkassist.dto.UserRegistrationDto;
import com.quickparkassist.model.PasswordResetToken;
import com.quickparkassist.model.User;
import com.quickparkassist.repository.TokenRepository;
import com.quickparkassist.repository.UserRepository;
import com.quickparkassist.service.PasswordResetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Random;
import org.springframework.ui.Model;
import org.springframework.security.crypto.password.PasswordEncoder;
@Controller
public class ForgotPasswordController {



    @Autowired
    PasswordResetService userDetailsService;


    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);

    Random random = new Random(1000);

    // email id form open handler

    @GetMapping("/forgotPassword")
    public String forgotPassword() {
        logger.info("Request to display forgot password page");

        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String forgotPassordProcess(@ModelAttribute UserRegistrationDto userDTO) {
        String output = "";
        User user = userRepository.findByEmail(userDTO.getEmail());
        if (user != null) {
            logger.info("User found for email: {}", userDTO.getEmail());
            output = userDetailsService.sendEmail(user);
            logger.info("Password reset email sent to: {}", userDTO.getEmail());

        }else {
            logger.warn("No user found with email: {}", userDTO.getEmail());
        }
        if (output.equals("success")) {
            logger.info("Password reset email sent successfully, redirecting to success page");

            return "redirect:/forgotPassword?success";
        }
        logger.error("Error occurred while sending password reset email for email: {}", userDTO.getEmail());

        return "redirect:/login?error";
    }
    @GetMapping("/resetPassword/{token}")
    public String resetPasswordForm(@PathVariable String token, Model model) {
        logger.info("Request to reset password with token: {}", token);

        PasswordResetToken reset = tokenRepository.findByToken(token);
        if (reset != null && !userDetailsService.hasExipred(reset.getExpiryDateTime())) {
            model.addAttribute("email", reset.getUser().getEmail());
            logger.info("Valid token, displaying reset password page for email: {}", reset.getUser().getEmail());

            return "resetPassword";  // This should load your reset password page
        }
        logger.warn("Invalid or expired token for password reset");

        return "redirect:/forgotPassword?error";
    }

    @PostMapping("/resetPassword")
    public String passwordResetProcess(@ModelAttribute UserRegistrationDto userDTO) {
        logger.info("Processing password reset for email: {}", userDTO.getEmail());

        User user = userRepository.findByEmail(userDTO.getEmail());
        if(user != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userRepository.save(user);
            logger.info("Password for user {} has been updated successfully", userDTO.getEmail());
        }
        else {
            logger.warn("No user found with email: {}", userDTO.getEmail());
        }
        return "redirect:/login";
    }


}
