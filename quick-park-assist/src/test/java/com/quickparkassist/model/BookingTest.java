package com.quickparkassist.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {
    @Test
    void testBookingModel() {
        Booking booking = new Booking();

        // Test setters
        booking.setId(1L);
        booking.setFullName("Brundha");
        booking.setDuration("2 hours");
        booking.setStartTime(LocalDateTime.of(2024, 12, 29, 14, 0));
        booking.setPrice("40.00");
        booking.setPaymentMethod("Cash");
        booking.setMobileNumber("1234");
        booking.setUserId(100L);
        booking.setUsername("Brundha");
        booking.setLocation("Downtown");
        booking.setStatus(Booking.Status.CONFIRMED);

        // Test getters
        assertEquals(1L, booking.getId());
        assertEquals("Brundha", booking.getFullName());
        assertEquals("2 hours", booking.getDuration());
        assertEquals(LocalDateTime.of(2024, 12, 29, 14, 0), booking.getStartTime());
        assertEquals("40.00", booking.getPrice());
        assertEquals("Cash", booking.getPaymentMethod());
        assertEquals("1234", booking.getMobileNumber());
        assertEquals(100L, booking.getUserId());
        assertEquals("Brundha", booking.getUsername());
        assertEquals("Downtown", booking.getLocation());
        assertEquals(Booking.Status.CONFIRMED, booking.getStatus());

        Spot parkingSpot = new Spot();
        booking.setSpot(parkingSpot);
        assertEquals(parkingSpot, booking.getSpot());
    }

    @Test
    void testBookingForPriya() {
        Booking booking = new Booking();

        booking.setId(2L);
        booking.setFullName("Priya");
        booking.setDuration("3 hours");
        booking.setStartTime(LocalDateTime.of(2024, 12, 30, 10, 0));
        booking.setPrice("50.00");
        booking.setPaymentMethod("Card");
        booking.setMobileNumber("5678");
        booking.setUserId(101L);
        booking.setUsername("Priya");
        booking.setLocation("Uptown");
        booking.setStatus(Booking.Status.PENDING);

        assertEquals(2L, booking.getId());
        assertEquals("Priya", booking.getFullName());
        assertEquals("3 hours", booking.getDuration());
        assertEquals(LocalDateTime.of(2024, 12, 30, 10, 0), booking.getStartTime());
        assertEquals("50.00", booking.getPrice());
        assertEquals("Card", booking.getPaymentMethod());
        assertEquals("5678", booking.getMobileNumber());
        assertEquals(101L, booking.getUserId());
        assertEquals("Priya", booking.getUsername());
        assertEquals("Uptown", booking.getLocation());
        assertEquals(Booking.Status.PENDING, booking.getStatus());
    }

    @Test
    void testBookingForDevi() {
        Booking booking = new Booking();

        booking.setId(3L);
        booking.setFullName("Devi");
        booking.setDuration("1 hour");
        booking.setStartTime(LocalDateTime.of(2024, 12, 28, 9, 30));
        booking.setPrice("30.00");
        booking.setPaymentMethod("UPI");
        booking.setMobileNumber("4321");
        booking.setUserId(102L);
        booking.setUsername("Devi");
        booking.setLocation("Midtown");
        booking.setStatus(Booking.Status.CANCELLED);

        assertEquals(3L, booking.getId());
        assertEquals("Devi", booking.getFullName());
        assertEquals("1 hour", booking.getDuration());
        assertEquals(LocalDateTime.of(2024, 12, 28, 9, 30), booking.getStartTime());
        assertEquals("30.00", booking.getPrice());
        assertEquals("UPI", booking.getPaymentMethod());
        assertEquals("4321", booking.getMobileNumber());
        assertEquals(102L, booking.getUserId());
        assertEquals("Devi", booking.getUsername());
        assertEquals("Midtown", booking.getLocation());
        assertEquals(Booking.Status.CANCELLED, booking.getStatus());
    }

    @Test
    void testBookingForSita() {
        Booking booking = new Booking();

        booking.setId(4L);
        booking.setFullName("Sita");
        booking.setDuration("4 hours");
        booking.setStartTime(LocalDateTime.of(2024, 12, 31, 18, 0));
        booking.setPrice("60.00");
        booking.setPaymentMethod("Cash");
        booking.setMobileNumber("8765");
        booking.setUserId(103L);
        booking.setUsername("Sita");
        booking.setLocation("City Center");
        booking.setStatus(Booking.Status.CONFIRMED);

        assertEquals(4L, booking.getId());
        assertEquals("Sita", booking.getFullName());
        assertEquals("4 hours", booking.getDuration());
        assertEquals(LocalDateTime.of(2024, 12, 31, 18, 0), booking.getStartTime());
        assertEquals("60.00", booking.getPrice());
        assertEquals("Cash", booking.getPaymentMethod());
        assertEquals("8765", booking.getMobileNumber());
        assertEquals(103L, booking.getUserId());
        assertEquals("Sita", booking.getUsername());
        assertEquals("City Center", booking.getLocation());
        assertEquals(Booking.Status.CONFIRMED, booking.getStatus());
    }
    @Test
    void testGetVehicleModel() {
        // Arrange
        Booking booking = new Booking();
        String expectedModel = "Tesla Model S";
        booking.setVehicleModel(expectedModel); // Set the vehicle model using the setter

        // Act
        String actualModel = booking.getVehicleModel(); // Get the vehicle model using the getter

        // Assert
        assertEquals(expectedModel, actualModel, "The vehicle model should match the expected value.");
    }

    @Test
    void testSetVehicleModel() {
        // Arrange
        Booking booking = new Booking();
        String expectedModel = "Nissan Leaf";

        // Act
        booking.setVehicleModel(expectedModel); // Set vehicle model via setter

        // Assert
        assertEquals(expectedModel, booking.getVehicleModel(), "The vehicle model should match the expected value.");
    }

    @Test
    void testGetVehicleNumber() {
        // Arrange
        Booking booking = new Booking();
        String expectedNumber = "ABC123";
        booking.setVehicleNumber(expectedNumber); // Set the vehicle number using the setter

        // Act
        String actualNumber = booking.getVehicleNumber(); // Get the vehicle number using the getter

        // Assert
        assertEquals(expectedNumber, actualNumber, "The vehicle number should match the expected value.");
    }

    @Test
    void testSetVehicleNumber() {
        // Arrange
        Booking booking = new Booking();
        String expectedNumber = "XYZ789";

        // Act
        booking.setVehicleNumber(expectedNumber); // Set vehicle number via setter

        // Assert
        assertEquals(expectedNumber, booking.getVehicleNumber(), "The vehicle number should match the expected value.");
    }

    @Test
    void testNullValues() {
        Booking booking = new Booking();

        // Check setting null values
        booking.setFullName(null);
        assertNull(booking.getFullName(), "Full name should be null");

        booking.setMobileNumber(null);
        assertNull(booking.getMobileNumber(), "Mobile number should be null");

        // Add similar checks for other fields as needed...
    }

    @Test
    void testStatusEnum() {
        Booking booking = new Booking();
        booking.setStatus(Booking.Status.PENDING);
        assertEquals(Booking.Status.PENDING, booking.getStatus());

        booking.setStatus(Booking.Status.COMPLETED);
        assertEquals(Booking.Status.COMPLETED, booking.getStatus());

        // Test transition from CONFIRMED to CANCELLED if applicable...
        booking.setStatus(Booking.Status.CANCELLED);
        assertEquals(Booking.Status.CANCELLED, booking.getStatus());
    }

}
