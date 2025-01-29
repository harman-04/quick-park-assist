
package com.quickparkassist.repository;


import org.springframework.stereotype.Repository;

import com.quickparkassist.model.AddonServiceBooking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface addonServiceBookingRepository extends JpaRepository<AddonServiceBooking,Long> {
    List<AddonServiceBooking> findByUserId(Long userId);
}

