package com.quickparkassist.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_addon_booking")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddonServiceBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addonServiceBookingID;

    @Column(name = "serviceids")
    private String serviceIDs;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "quantities")
    private String quantities;


    public AddonServiceBooking(Long addonServiceBookingID, String quantities, String serviceIDs) {
        this.addonServiceBookingID = addonServiceBookingID;
        this.quantities = quantities;
        this.serviceIDs = serviceIDs;
    }

    public String getServiceIDs() {
        return serviceIDs;
    }

    public String getQuantities() {
        return quantities;
    }
    public void setServiceIDs(String serviceIDs) {
        this.serviceIDs = serviceIDs;
    }

    public void setQuantities(String quantities) {
        this.quantities = quantities;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
