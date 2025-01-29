package com.quickparkassist.repository;



import com.quickparkassist.model.EVChargingStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EVChargingStationRepository extends JpaRepository<EVChargingStation, Long> {

}
