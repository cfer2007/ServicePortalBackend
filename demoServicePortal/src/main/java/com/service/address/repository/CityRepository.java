package com.service.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.service.address.model.City;

import java.util.List;


public interface CityRepository extends JpaRepository<City, Long>{
	
	@Query(value = "select * from city where region_id =?1", nativeQuery = true)
	List<City> getCitiesByRegion(Long id);
}