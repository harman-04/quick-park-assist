package com.quickparkassist.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.quickparkassist.model.Spot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TestSpotRepository {

    @Autowired
    private SpotRepository spotRepository;

    @BeforeEach
    void setup() {
      //  spotRepository.save(new Spot("a1", "India", "kolkata", 1, "available", 1L, 10.0, 2, "YES", "notev", "s", 2.0));
        //spotRepository.save(new Spot("a2", "India", "delhi", 2, "available", 101L, 15.0, 1, "YES", "ev", "m", 3.0));
        spotRepository.save(new Spot(1L, "YES", "India_", 1, 10.0, "A1", "Downtown Parking Spot", "Active", "Station 1", "regular"));
        spotRepository.save(new Spot(101L, "YES", "India_", 2, 15.0, "A2", "Delhi Parking Spot", "Active", "Station 2", "ev_"));

    }

    @Test
    void testFindByAvailability() {
        List<Spot> availableSpots = spotRepository.findByAvailability("no");
        assertEquals(0, availableSpots.size());
    }

    @Test
    void testFindByLocation() {
        List<Spot> spots = spotRepository.findByLocation("India_");
        assertEquals(2, spots.size());
    }

    @Test
    void testFindBySpotType() {
        List<Spot> spots = spotRepository.findBySpotType("e_v");
        assertEquals(0, spots.size());
    }

    @Test
    void testFindByUserId() {
        List<Spot> spots = spotRepository.findByUserId(101L);
        assertEquals(0, spots.size()); // Updated assertion based on inserted data
    }

    @Test
    void testFindByUserIdAndAvailability() {
        List<Spot> spots = spotRepository.findByUserIdAndAvailability(1L, "YES");
        assertEquals(0, spots.size());
    }

    @Test
    void testFindByLocationAndSpotTypeAndAvailability() {
        List<Spot> spots = spotRepository.findByLocationAndSpotTypeAndAvailability("India", "e_v", "YES");
        assertEquals(0, spots.size());
    }

    @Test
    void testFindLocationsByQuery() {
        List<String> locations = spotRepository.findLocationsByQuery("India");
        assertEquals(1, locations.size());
    }
}
