package com.quickparkassist.controller;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Get the HTTP error status code
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String errorMessage = (String) request.getAttribute("javax.servlet.error.message");

        // Add status code and error message to the model
        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorMessage", errorMessage);

        // Handle specific error codes
        if (statusCode != null) {
            if (statusCode == 404) {
                return "errorsection/404"; // Render 404.html
            } else if (statusCode == 500) {
                return "errorsection/500"; // Render 500.html if you create one
            }
        }

        return "errorsection/error"; // Render error.html for all other errors
    }

    @Override
    public String getErrorPath() {
        return "/errorsection/error";
    }
}
