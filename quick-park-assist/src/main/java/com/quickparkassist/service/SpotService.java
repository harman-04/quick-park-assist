package com.quickparkassist.service;

import com.quickparkassist.model.Spot;
import com.quickparkassist.repository.SpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpotService {

    @Autowired
    private SpotRepository spotRepository;

    // Get all available spots
    public List<Spot> getAvailableSpots() {
        return spotRepository.findByAvailability("yes");
    }

    // Get all unavailable spots
    public List<Spot> getUnavailableSpots() {
        return spotRepository.findByAvailability("no");
    }

    // Get spots by both location and availability
    public List<Spot> getSpotsByLocationAndAvailability(String location, String spotType, String availability) {
        return spotRepository.findByLocationAndSpotTypeAndAvailability(location, spotType, availability);
    }

    // Get spots by location only (ignoring availability)
    public List<Spot> getSpotsByLocation(String location) {
        return spotRepository.findByLocation(location);
    }

    // Get all spots ordered by location
    public List<Spot> getAllSpotsOrderedByLocation() {
        return spotRepository.findAllByOrderByLocationAsc();
    }
    // Method to fetch spots with a specific spot type
    public List<Spot> getSpotsByType(String spotType) {
        return spotRepository.findBySpotType(spotType); // Use the repository method to fetch spots
    }
    public List<Spot> getAvailableSpotsByUserId(Long userId) {
        // Fetch the available spots owned by a specific user
        return spotRepository.findByUserId(userId);
    }
    // Get all available spots by user ID
    public List<Spot> getAvailableSpotsByUser_Id(Long userId) {
        return spotRepository.findByUserIdAndAvailability(userId, "yes"); // Only available spots
    }

    // Get all unavailable spots by user ID
    public List<Spot> getUnavailableSpotsByUserId(Long userId) {
        return spotRepository.findByUserIdAndAvailability(userId, "no"); // Only unavailable spots
    }

    // Get a spot by its ID
    public Spot getSpotById(Long id) {
        return spotRepository.findById(id).orElse(null);
    }

    // Save a new spot
    public void saveSpot(Spot spot) {
        spotRepository.save(spot);
    }

    // Update an existing spot
    public void updateSpot(Spot spot) {
        spotRepository.save(spot);
    }

    // Remove a spot by ID
    public void removeSpot(Long id) {
        spotRepository.deleteById(id);
    }

    public void setSpotRepository(SpotRepository spotRepository) {
        this.spotRepository = spotRepository;
    }

}
