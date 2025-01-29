package com.quickparkassist.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EVChargingStationTest {

    private EVChargingStation evChargingStation;

    @BeforeEach
    void setUp() {
        evChargingStation = new EVChargingStation();
    }

    @Test
    void testGettersAndSetters() {
        Long id = 1L;
        String name = "Super Charger";
        String location = "123 Electric Ave";
        Long chargingStationId = 101L;

        evChargingStation.setId(id);
        evChargingStation.setName(name);
        evChargingStation.setLocation(location);
        evChargingStation.setChargingStationId(chargingStationId);

        // Assertions
        assertEquals(id, evChargingStation.getId());
        assertEquals(name, evChargingStation.getName());
        assertEquals(location, evChargingStation.getLocation());
        assertEquals(chargingStationId, evChargingStation.getChargingStationId());
    }
}