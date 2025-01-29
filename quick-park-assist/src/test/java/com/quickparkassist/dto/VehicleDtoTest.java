package com.quickparkassist.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class VehicleDtoTest {

    @Test
    void testDefaultConstructor() {
        // Arrange
        VehicleDto vehicleDto = new VehicleDto();

        // Assert
        assertNull(vehicleDto.getVehicleNumber(), "Vehicle number should be null.");
        assertNull(vehicleDto.getVehicleModel(), "Vehicle model should be null.");
        assertNull(vehicleDto.getHasElectric(), "Has electric status should be null.");
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange
        String expectedVehicleNumber = "ABC123";
        String expectedVehicleModel = "Tesla Model S";
        String expectedHasElectric = "yes";

        // Act
        VehicleDto vehicleDto = new VehicleDto(expectedVehicleNumber, expectedVehicleModel, expectedHasElectric);

        // Assert
        assertEquals(expectedVehicleNumber, vehicleDto.getVehicleNumber(), "Vehicle number should match.");
        assertEquals(expectedVehicleModel, vehicleDto.getVehicleModel(), "Vehicle model should match.");
        assertEquals(expectedHasElectric, vehicleDto.getHasElectric(), "Has electric status should match.");
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        VehicleDto vehicleDto = new VehicleDto();

        // Act
        vehicleDto.setVehicleNumber("XYZ789");
        vehicleDto.setVehicleModel("Nissan Leaf");
        vehicleDto.setHasElectric("yes");

        // Assert
        assertEquals("XYZ789", vehicleDto.getVehicleNumber(), "Vehicle number should match.");
        assertEquals("Nissan Leaf", vehicleDto.getVehicleModel(), "Vehicle model should match.");
        assertEquals("yes", vehicleDto.getHasElectric(), "Has electric status should match.");
    }
}
