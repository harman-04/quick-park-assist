package com.quickparkassist.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;
import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomErrorControllerTest {


    @Test
    void testHandleError_404() {
        // Arrange
        CustomErrorController controller = new CustomErrorController();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Model model = Mockito.mock(Model.class);

        when(request.getAttribute("javax.servlet.error.status_code")).thenReturn(404);
        when(request.getAttribute("javax.servlet.error.message")).thenReturn("Not Found");

        // Act
        String viewName = controller.handleError(request, model);

        // Assert
        assertEquals("errorsection/404", viewName);
        verify(model).addAttribute("statusCode", 404);
        verify(model).addAttribute("errorMessage", "Not Found");
    }

    @Test
    void testHandleError_500() {
        // Arrange
        CustomErrorController controller = new CustomErrorController();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Model model = Mockito.mock(Model.class);

        when(request.getAttribute("javax.servlet.error.status_code")).thenReturn(500);
        when(request.getAttribute("javax.servlet.error.message")).thenReturn("Internal Server Error");

        // Act
        String viewName = controller.handleError(request, model);

        // Assert
        assertEquals("errorsection/500", viewName);
        verify(model).addAttribute("statusCode", 500);
        verify(model).addAttribute("errorMessage", "Internal Server Error");
    }

    @Test
    void testHandleError_Other() {
        // Arrange
        CustomErrorController controller = new CustomErrorController();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Model model = Mockito.mock(Model.class);

        when(request.getAttribute("javax.servlet.error.status_code")).thenReturn(403); // Example of another error code
        when(request.getAttribute("javax.servlet.error.message")).thenReturn("Forbidden");

        // Act
        String viewName = controller.handleError(request, model);

        // Assert
        assertEquals("errorsection/error", viewName);
        verify(model).addAttribute("statusCode", 403);
        verify(model).addAttribute("errorMessage", "Forbidden");
    }

    @Test
    void testGetErrorPath() {
        // Arrange
        CustomErrorController controller = new CustomErrorController();

        // Act
        String path = controller.getErrorPath();

        // Assert
        assertEquals("/errorsection/error", path);
    }
    @Test
    void testHandleError_NullStatusCode() {
        // Arrange
        CustomErrorController controller = new CustomErrorController();
        HttpServletRequest request = mock(HttpServletRequest.class);
        Model model = mock(Model.class);

        when(request.getAttribute("javax.servlet.error.status_code")).thenReturn(null);
        when(request.getAttribute("javax.servlet.error.message")).thenReturn(null);

        // Act
        String result = controller.handleError(request, model);

        // Assert
        assertEquals("errorsection/error", result);
        verify(model).addAttribute("statusCode", null);
        verify(model).addAttribute("errorMessage", null);
    }
}
