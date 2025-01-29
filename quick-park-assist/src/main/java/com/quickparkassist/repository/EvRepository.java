package com.quickparkassist.repository;




import org.springframework.data.jpa.repository.JpaRepository;

import com.quickparkassist.model.Evmodel;

public interface EvRepository extends JpaRepository<Evmodel, Long> {
}
