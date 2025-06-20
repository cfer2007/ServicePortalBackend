package com.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.service.model.Professional;

public interface ProfessionalRepository extends JpaRepository<Professional, Long> {
	boolean existsByUserId(Long id);
	
	@Query(value = "select * from professional where user_id = (select id from users where email = ?1)", nativeQuery = true)
	Professional findByEmail(@Param("id")String email);	
	
	@Query(value = "SELECT p.*\n"
			+ "FROM professional p\n"
			+ "INNER JOIN professional_skill ps\n"
			+ "  ON p.professional_id = ps.professional_id\n"
			+ "WHERE ps.skill_id = :id ", nativeQuery = true)
	List<Professional> findProfessionalsBySkill(@Param("id")Long id);
}
