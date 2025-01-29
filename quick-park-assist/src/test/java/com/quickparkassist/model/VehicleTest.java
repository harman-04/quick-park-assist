package com.quickparkassist.model;
import com.quickparkassist.model.User;
import com.quickparkassist.model.Vehicle;
import com.quickparkassist.repository.UserRepository;
import com.quickparkassist.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class VehicleTest {

    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        // Initialize the Vehicle object before each test
        vehicle = new Vehicle();
    }

    @Test
    void testDefaultConstructor() {
        // Verify that the default constructor creates a Vehicle object with default values
        assertNotNull(vehicle);
        assertNull(vehicle.getVehicleNumber());
        assertNull(vehicle.getVehicleModel());
        assertNull(vehicle.getHasElectric());
        assertNull(vehicle.getUser());
    }

    @Test
    void testParameterizedConstructor() {
        // Test the constructor with parameters
        Long id = 1L;
        String model = "Tesla Model 3";
        String number = "ABC123";

        vehicle = new Vehicle(id, model, number);

        // Verify that the constructor initializes the object correctly
        assertEquals(id, vehicle.getId());
        assertEquals(model, vehicle.getVehicleModel());
        assertEquals(number, vehicle.getVehicleNumber());
    }

    @Test
    void testSettersAndGetters() {
        // Set values using setters
        vehicle.setVehicleNumber("XYZ789");
        vehicle.setVehicleModel("Ford Mustang");
        vehicle.setHasElectric("Yes");

        // Verify that getters return the correct values
        assertEquals("XYZ789", vehicle.getVehicleNumber());
        assertEquals("Ford Mustang", vehicle.getVehicleModel());
        assertEquals("Yes", vehicle.getHasElectric());
    }

    @Test
    void testSetUser() {
        // Create a mock user object
        User user = new User("testuser@example.com");

        // Set the user for the vehicle
        vehicle.setUser(user);

        // Verify that the user is set correctly
        assertEquals(user, vehicle.getUser());
    }

    @Test
    void testVehicleUserRelationship() {
        // Create mock User object (simplified for testing)
        User user = new User("testuser@example.com");
        vehicle.setUser(user);

        // Test if the vehicle correctly associates with the user
        assertEquals(user, vehicle.getUser());
        assertNotNull(vehicle.getUser());
    }

    @Test
    void testVehicleWithNullValues() {
        // Create a vehicle object with null values
        vehicle.setVehicleNumber(null);
        vehicle.setVehicleModel(null);
        vehicle.setHasElectric(null);

        // Verify that the vehicle object handles null values correctly
        assertNull(vehicle.getVehicleNumber());
        assertNull(vehicle.getVehicleModel());
        assertNull(vehicle.getHasElectric());
    }

}


