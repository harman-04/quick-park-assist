package com.quickparkassist.model;


import javax.persistence.*;

@Entity
@Table(name = "parkingspots")

public class Spot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spotId;

    private String spotName;
    private String location;
    private String station;
    private int slot;
    private String spotStatus;  // Status of the parking spot (e.g., available, reserved, occupied)
    private String description;
    private Long userId;

    @Column(name = "price_per_hour")
    private double pricePerHour;
    private int ownerId;        // The ID of the owner (foreign key reference to users table)

    @Column(name = "availability")
    private String availability;
    @Column(name = "spot_type", nullable = false)
    private String spotType;


    // EV-specific fields
    private String chargerType;      // For EV spots (e.g., Type 2, CCS)
    private Double powerCapacity;    // kW (optional for normal spots)

    // Default constructor required by Hibernate
    public Spot() {
        // No-argument constructor (default constructor)
    }

    public Spot(Long spotId, String availability, String location, int slot, double pricePerHour, String spotName, String description, String spotStatus, String station, String spotType) {
        this.spotId = spotId;
        this.availability = availability;
        this.location = location;
        this.slot = slot;
        this.pricePerHour = pricePerHour;
        this.spotName = spotName;
        this.spotStatus = spotStatus;
        this.description = description;
        this.station = station;
        this.spotType = spotType;
    }


    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public Long getSpotId() {
        return spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }


    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public String getSpotStatus() {
        return spotStatus;
    }

    public void setSpotStatus(String spotStatus) {
        this.spotStatus = spotStatus;
    }

    public String getSpotType() {
        return spotType;
    }

    public void setSpotType(String spotType) {
        this.spotType = spotType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getChargerType() {
        return chargerType;
    }

    public void setChargerType(String chargerType) {
        this.chargerType = chargerType;
    }

    public Double getPowerCapacity() {
        return powerCapacity;
    }

    public void setPowerCapacity(Double powerCapacity) {
        this.powerCapacity = powerCapacity;
    }

    public Object getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int i) {
        this.ownerId=i;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
