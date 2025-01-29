package com.quickparkassist.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestSpot {

    private Spot spot;

//    @BeforeEach
//    void setUp() {
//        spot = new Spot(
//                "Central Parking", "Downtown", "Station A", 5, "Available",
//                101L, 20.5, 202, "Open", "Regular", "Type 2", 7.5
//        );
//    }
@BeforeEach
void setUp() {
    spot = new Spot(1L, "YES", "Downtown", 101, 20.00, "A1", "Downtown Parking Spot", "Active", "Station 1","Regular");
//            "Central Parking", "Downtown", "Station A", 5, "Available",
//            101L, 20.5, 202, "Open", "Regular", "Type 2", 7.5
//    );
}



    @Test
    void testConstructorAssignment() {
        assertEquals(1L, spot.getSpotId()); // spotId is 1L
        assertEquals("YES", spot.getAvailability()); // Availability is "YES"
        assertEquals("Downtown", spot.getLocation()); // Location is "Downtown"
        assertEquals(101, spot.getSlot()); // Slot is 101
        assertEquals(20.00, spot.getPricePerHour()); // Price per hour is 20.00
        assertEquals("A1", spot.getSpotName()); // Spot name is "A1"
        assertEquals("Downtown Parking Spot", spot.getDescription()); // Description is "Downtown Parking Spot"
        assertEquals("Active", spot.getSpotStatus()); // Spot status is "Active"
        assertEquals("Station 1", spot.getStation()); // Station is "Station 1"
        assertEquals("Regular", spot.getSpotType());
//        assertEquals("Central Parking", spot.getSpotName());
//        assertEquals("Downtown", spot.getLocation());
//        assertEquals("Station A", spot.getStation());
//        assertEquals(5, spot.getSlot());
//        assertEquals("Available", spot.getSpotStatus());
//        assertEquals(101L, spot.getUserId());
//        assertEquals(20.5, spot.getPricePerHour());
//        assertEquals(202, spot.getOwnerId());
//        assertEquals("Open", spot.getAvailability());
//        assertEquals("Regular", spot.getSpotType());
//        assertEquals("Type 2", spot.getChargerType());
//        assertEquals(7.5, spot.getPowerCapacity());
    }

    @Test
    void testSetters() {
        spot.setSpotName("Updated Parking");
        assertEquals("Updated Parking", spot.getSpotName());

        spot.setLocation("New Location");
        assertEquals("New Location", spot.getLocation());

        spot.setStation("Station B");
        assertEquals("Station B", spot.getStation());

        spot.setSlot(10);
        assertEquals(10, spot.getSlot());

        spot.setSpotStatus("Occupied");
        assertEquals("Occupied", spot.getSpotStatus());

        spot.setUserId(202L);
        assertEquals(202L, spot.getUserId());

        spot.setPricePerHour(25.0);
        assertEquals(25.0, spot.getPricePerHour());

        spot.setOwnerId(303);
        assertEquals(303, spot.getOwnerId());

        spot.setAvailability("Closed");
        assertEquals("Closed", spot.getAvailability());

        spot.setSpotType("EV");
        assertEquals("EV", spot.getSpotType());

        spot.setChargerType("CCS");
        assertEquals("CCS", spot.getChargerType());

        spot.setPowerCapacity(15.0);
        assertEquals(15.0, spot.getPowerCapacity());

    }

    @Test
    void testDefaultConstructor() {
        Spot defaultSpot = new Spot();
        assertNull(defaultSpot.getSpotName());
        assertNull(defaultSpot.getLocation());
        assertNull(defaultSpot.getStation());
        assertNull(defaultSpot.getSpotStatus());
        assertNull(defaultSpot.getAvailability());
        assertNull(defaultSpot.getSpotType());
        assertNull(defaultSpot.getChargerType());
        assertNull(defaultSpot.getPowerCapacity());
        assertNull(defaultSpot.getUserId());
        assertEquals(0.0,defaultSpot.getPricePerHour());
        assertEquals(0,defaultSpot.getOwnerId());

    }
    @Test
    void testSetDescription() {
        // Arrange
        String expectedDescription = "This is a test description";

        // Act
        spot.setDescription(expectedDescription);

        // Assert
        assertEquals(expectedDescription, spot.getDescription(), "The description should be set correctly");
    }
}
