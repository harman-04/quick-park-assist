package com.quickparkassist.model;

import lombok.*;


import javax.persistence.*;
@Entity
@Table(name = "tbl_addon")
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddonService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ServiceID;
    private String name;
    private String description;
    private float price;
    private float duration;
    private boolean isActive;

    @Column(name = "owner_id")
    private Long ownerId;

    // All-args constructor
    public AddonService(Long serviceID, String name, String description, float price, float duration, boolean isActive) {
        this.ServiceID = serviceID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.isActive = isActive;
    }
    // Custom getter for compatibility
    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Long getServiceID() {
        return ServiceID;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getServiceName() {
        return name;
    }

    public void setServiceName(String name) {
        this.name = name;
    }


}
