package com.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.service.model.ProfessionalAvailability;

public interface ProfessionalAvailabilityRepository extends JpaRepository<ProfessionalAvailability, Long>{

	@Query(value = "select * from professional_availability where professional_id = ?1", nativeQuery = true)
	List<ProfessionalAvailability> findByProfesionalId(@Param("id")Long id);
	//List<ProfessionalAvailability> getProfessionalAvailabilitiesByProfessional(@Param("id")Long id);
}
