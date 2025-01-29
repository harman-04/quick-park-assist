package com.quickparkassist.model;



import javax.persistence.*;

@Entity
@Table(name = "ev_charging_stations")
public class EVChargingStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private Long chargingStationId;  // For storing the selected station ID

    // Getter and Setter
    public Long getChargingStationId() {
        return chargingStationId;
    }

    public void setChargingStationId(Long chargingStationId) {
        this.chargingStationId = chargingStationId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
