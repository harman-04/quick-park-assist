package com.quickparkassist.service;



import com.quickparkassist.model.EVChargingStation;
import com.quickparkassist.repository.EVChargingStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EVChargingStationService {

    @Autowired
    private EVChargingStationRepository chargingStationRepository;


    public List<EVChargingStation> getAllStations() {
        return chargingStationRepository.findAll(); // Ensure correct return type
    }

    // Get a charging station by ID
    public Optional<EVChargingStation> getStationById(Long id) {
        return chargingStationRepository.findById(id);
    }

    // Save a new charging station
    public EVChargingStation saveStation(EVChargingStation chargingStation) {
        return chargingStationRepository.save(chargingStation);
    }

    // Delete a charging station by ID
    public void deleteStation(Long id) {
        chargingStationRepository.deleteById(id);
    }
}
