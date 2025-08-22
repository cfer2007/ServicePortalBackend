package com.service.repository;

import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.service.enums.ProfileStatus;
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
	
	@Query("""
		    SELECT p
		    FROM professional p          
		      LEFT JOIN p.user u
		    WHERE p.status IN (:statuses)
		      AND (
		        :q IS NULL OR :q = '' OR
		        LOWER(p.name) LIKE LOWER(CONCAT('%', :q, '%')) OR
		        LOWER(p.lastName) LIKE LOWER(CONCAT('%', :q, '%')) OR
		        (u IS NOT NULL AND LOWER(u.email) LIKE LOWER(CONCAT('%', :q, '%')))
		      )
		  """)
	Page<Professional> searchQueue(@Param("statuses") java.util.List<ProfileStatus> statuses,
			                               @Param("q") String q,
			                               Pageable pageable);
}
