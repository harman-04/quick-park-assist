package com.quickparkassist.repository;

import com.quickparkassist.model.AddonService;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface addonRepository extends JpaRepository<AddonService,Long> {
    List<AddonService> findByOwnerId(Long ownerId);
}
