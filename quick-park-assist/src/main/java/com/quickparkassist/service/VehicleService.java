package com.quickparkassist.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quickparkassist.dto.VehicleDto;
import com.quickparkassist.model.User;
import com.quickparkassist.model.Vehicle;
import com.quickparkassist.repository.UserRepository;
import com.quickparkassist.repository.VehicleRepository;

import javax.persistence.EntityNotFoundException;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public VehicleService(VehicleRepository vehicleRepository, UserRepository userRepository) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void save(VehicleDto vehicleDto) {
        // Get the currently logged-in user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // Assuming the email is used as a unique identifier

        // Fetch user by email (use findIdByEmail to retrieve user)
        User user = userRepository.findIdByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create a new Vehicle object
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber(vehicleDto.getVehicleNumber());
        vehicle.setVehicleModel(vehicleDto.getVehicleModel());
        vehicle.setHasElectric(vehicleDto.getHasElectric());

        // Set the current user to the vehicle
        vehicle.setUser(user);

        // Save the vehicle
        vehicleRepository.save(vehicle);
    }
    public void deleteVehicleById(Long id) {
        if (vehicleRepository.existsById(id)) {
            vehicleRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Vehicle not found with ID: " + id);
        }
    }
}
