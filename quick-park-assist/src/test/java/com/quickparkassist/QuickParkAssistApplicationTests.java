package com.quickparkassist;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;

@SpringBootTest
class QuickParkAssistApplicationTests {
	@Autowired
	private ApplicationContext context;

	@Test
	void testMain() {
		// Mock the SpringApplication class
		SpringApplication springApplication = Mockito.mock(SpringApplication.class);
		// Call the main method
		QuickParkAssistApplication.main(new String[]{});

		// Assertion to check that the application context is not null
		assertNotNull(context, "Application context should not be null.");

		// Assertion to check if a specific bean exists in the context
		assertTrue(context.containsBean("bookingService"), "The application context should contain the 'bookingService' bean.");
	}

	@Test
	void contextLoads() {
		assertNotNull(context, "Application context should not be null.");
	}

}
