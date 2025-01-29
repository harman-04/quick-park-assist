package com.quickparkassist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vehicleNumber; // Unique number of the vehicle
    private String vehicleModel;  // Model of the vehicle
    private String hasElectric;   // Indicates if the vehicle is electric

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to the User table
    @JsonIgnore
    private User user;

    // Default constructor
    public Vehicle() {}

    // Constructor with 3 parameters
    public Vehicle(Long id, String model, String number) {
        this.id = id;
        this.vehicleModel = model;
        this.vehicleNumber = number;
    }
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getHasElectric() {
        return hasElectric;
    }

    public void setHasElectric(String hasElectric) {
        this.hasElectric = hasElectric;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}