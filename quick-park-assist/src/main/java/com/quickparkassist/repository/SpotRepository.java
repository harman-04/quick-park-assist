package com.quickparkassist.repository;

//import com.quickpark.booking_spot.model.ParkingSpot;
import com.quickparkassist.model.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpotRepository extends JpaRepository<Spot, Long> {

    // Find spots by availability status
    List<Spot> findByAvailability(String availability);

    List<Spot> findByLocationAndSpotTypeAndAvailability(String location, String spotType, String availability);

    // Find spots by location only (regardless of availability)
    List<Spot> findByLocation(String location);

    // Optional: Find all spots with specific availability status (Available or Unavailable)
    List<Spot> findByAvailabilityOrderByLocationAsc(String availability);

    // Optional: Find all spots (regardless of availability) and order by location
    List<Spot> findAllByOrderByLocationAsc();

    List<Spot> findBySpotType(String spotType);

    @Query("SELECT DISTINCT s.location FROM Spot s WHERE s.location LIKE %:query%")
    List<String> findLocationsByQuery(@Param("query") String query);

    List<Spot> findByUserId(Long userId);
    // Find spots by userId and availability status
    List<Spot> findByUserIdAndAvailability(Long userId, String availability);


}
