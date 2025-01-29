package com.quickparkassist.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testAddVehicle() {
        Vehicle vehicle = new Vehicle(); // Assuming Vehicle has a default constructor
        user.addVehicle(vehicle);

        assertEquals(1, user.getVehicles().size());
        assertEquals(user, vehicle.getUser()); // Assuming Vehicle has a getUser method
    }


    @Test
    void testRemoveVehicle() {
        Vehicle vehicle = new Vehicle();
        user.addVehicle(vehicle);

        user.removeVehicle(vehicle);

        assertEquals(0, user.getVehicles().size());
        assertNull(vehicle.getUser()); // Ensure the vehicle's user is null after removal
    }
    @Test
    void testSetVehicles() {
        // Arrange
        List<Vehicle> vehicleList = new ArrayList<>();
        Vehicle vehicle1 = new Vehicle(); // Assuming Vehicle has a default constructor
        Vehicle vehicle2 = new Vehicle();
        vehicleList.add(vehicle1);
        vehicleList.add(vehicle2);

        User user = new User(); // Create a new user instance

        // Act
        user.setVehicles(vehicleList); // Set the vehicles list to the user

        // Assert
        assertEquals(2, user.getVehicles().size(), "Vehicle list size should be 2");
        assertTrue(user.getVehicles().contains(vehicle1), "User's vehicle list should contain vehicle1");
        assertTrue(user.getVehicles().contains(vehicle2), "User's vehicle list should contain vehicle2");
    }


    @Test
    void testSetAndGetFullName() {
        String fullName = "John Doe";
        user.setFullName(fullName);

        assertEquals(fullName, user.getFullName());
    }

    @Test
    void testSetAndGetPhoneNumber() {
        String phoneNumber = "123-456-7890";
        user.setPhoneNumber(phoneNumber);

        assertEquals(phoneNumber, user.getPhoneNumber());
    }

    @Test
    void testSetAndGetAvailability() {
        String availability = "Available";
        user.setAvailability(availability);

        assertEquals(availability, user.getAvailability());
    }

    @Test
    void testSetAndGetAddress() {
        String address = "123 Main St";
        user.setAddress(address);

        assertEquals(address, user.getAddress());
    }

    @Test
    void testSetAndGetRole() {
        String role = "Admin";
        user.setRole(role);

        assertEquals(role, user.getRole());
    }

    @Test
    void testSetAndGetPasswordResetToken() {
        PasswordResetToken token = new PasswordResetToken(); // Assuming PasswordResetToken has a default constructor
        user.setPasswordResetToken(token);

        assertEquals(token, user.getPasswordResetToken());
    }
}
