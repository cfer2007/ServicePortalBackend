package com.service.address.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.service.address.model.Area;

public interface AreaRepository extends JpaRepository<Area, Long>{

	@Query(value = "select * from area where city_id =?1", nativeQuery = true)
	List<Area> getAreasByCity(Long id);
}
