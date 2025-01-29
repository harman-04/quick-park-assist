package com.quickparkassist.dto;



public class UserRegistrationDto {

    private String fullName;
    private String email;
    private String password;
    private String availability;
    private String phoneNumber;
    private String address;

    private String role; // New field for role

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    // Default constructor
    public UserRegistrationDto() {
    }

    // Constructor with role field
    public UserRegistrationDto(String fullName, String email, String password, String availability,String phoneNumber,String address, String role) {
        this.fullName = fullName;

        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.availability = availability;
        this.address = address;

        this.role = role; // Assigning role
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

    // Getter and Setter methods for the role field
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
