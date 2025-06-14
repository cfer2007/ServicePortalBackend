package com.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.service.dto.IProfessionDTO;
import com.service.model.Profession;

public interface ProfessionRepository extends JpaRepository<Profession, Long>{
	
	@Query(value = "select profession_id as professionId, name from profession where category_id=?1", nativeQuery = true)
	List<IProfessionDTO> findByCategoryId(@Param("id")Long id);
}
