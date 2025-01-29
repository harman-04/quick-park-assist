package com.quickparkassist;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServletInitializerTest {

    @Test
    void testConfigure() {
        // Arrange
        ServletInitializer servletInitializer = new ServletInitializer();
        SpringApplicationBuilder applicationBuilder = Mockito.mock(SpringApplicationBuilder.class);
        SpringApplicationBuilder returnedBuilder = Mockito.mock(SpringApplicationBuilder.class);

        // Mock the behavior of the application builder
        Mockito.when(applicationBuilder.sources(QuickParkAssistApplication.class)).thenReturn(returnedBuilder);

        // Act
        SpringApplicationBuilder result = servletInitializer.configure(applicationBuilder);

        // Assert
        assertEquals(returnedBuilder, result, "The returned SpringApplicationBuilder should match the mocked builder.");
        Mockito.verify(applicationBuilder).sources(QuickParkAssistApplication.class); // Verify sources method was called
    }
}
