package com.quickparkassist.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EvmodelTest {

    private Evmodel evmodel;

    @BeforeEach
    void setUp() {
        evmodel = new Evmodel();
    }

    @Test
    void testGettersAndSetters() {
        Long id = 1L;
        Integer reservationId = 100;
        String location = "Test Location";
        double duration = 2.5;
        String spotName = "Test Spot";
        Long evSpot = 10L;
        Date reservationDate = new Date();
        String startTime = "10:00";
        String endTime = "12:30";
        String vehicleId = "ABC123";
        Long userId = 2L;

        evmodel.setId(id);
        evmodel.setReservation_id(reservationId);
        evmodel.setLocation(location);
        evmodel.setDuration(duration);
        evmodel.setSpotName(spotName);
        evmodel.setEvSpot(evSpot);
        evmodel.setReservation_Date(reservationDate);
        evmodel.setStart_time(startTime);
        evmodel.setEnd_time(endTime);
        evmodel.setVehicle_id(vehicleId);
        evmodel.setUserId(userId);

        // Assertions
        assertEquals(id, evmodel.getId());
        assertEquals(reservationId, evmodel.getReservation_id());
        assertEquals(location, evmodel.getLocation());
        assertEquals(duration, evmodel.getDuration());
        assertEquals(spotName, evmodel.getSpotName());
        assertEquals(evSpot, evmodel.getEvSpot());
        assertEquals(reservationDate, evmodel.getReservation_Date());
        assertEquals(startTime, evmodel.getStart_time());
        assertEquals(endTime, evmodel.getEnd_time());
        assertEquals(vehicleId, evmodel.getVehicle_id());
        assertEquals(userId, evmodel.getUserId());
    }

    @Test
    void testCalculateEndTime() {
        LocalTime startTime = LocalTime.of(10, 0); // 10:00 AM
        double duration = 2.5; // 2.5 hours

        evmodel.setDuration(duration);

        LocalTime expectedEndTime = startTime.plusHours(2).plusMinutes(30); // 12:30 PM
        LocalTime actualEndTime = evmodel.calculateEndTime(startTime);

        assertEquals(expectedEndTime, actualEndTime);
    }
}
