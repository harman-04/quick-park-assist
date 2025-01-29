package com.quickparkassist.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String password;
    private String availability;

    private String phoneNumber;
    private String address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Vehicle> vehicles = new ArrayList<>();

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicle.setUser(this);
    }

    public void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
        vehicle.setUser(null);
    }
    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    // Setter for vehicles
    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }


    private String role;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PasswordResetToken passwordResetToken;

    public PasswordResetToken getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(PasswordResetToken passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    // Add this constructor for testing purposes
    public User(String email) {
        this.email = email;
    }

    public User() {
    }

    // Modified constructor
    public User(String fullName, String email, String password, String availability, String phoneNumber,
                String address, String role) {
        super();
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.availability = availability;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;  // Returning role as String
    }

    public void setRole(String role) {
        this.role = role;  // Setting role as String
    }

}
