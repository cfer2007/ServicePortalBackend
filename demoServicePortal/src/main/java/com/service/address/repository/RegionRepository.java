package com.service.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.address.model.Region;

public interface RegionRepository extends JpaRepository<Region, Long>{

}
