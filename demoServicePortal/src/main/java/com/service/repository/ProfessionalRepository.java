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
	Page<Professional> searchQueue(@Param("statuses") java.util.List<ProfileStatus> statuses, @Param("q") String q, Pageable pageable);
	
	
	@Query(value = """
		    SELECT DISTINCT p.*
		    FROM professional p
		    JOIN profession pr ON p.profession_id = pr.profession_id
		    JOIN category c ON pr.category_id = c.category_id
		    LEFT JOIN professional_skill ps ON p.professional_id = ps.professional_id
		    LEFT JOIN skill s ON ps.skill_id = s.skill_id
		    LEFT JOIN users u ON p.user_id = u.id
		    WHERE p.status = 'ACTIVE'
		      AND (
		        (:professionId IS NULL AND (:categoryId IS NULL OR c.category_id = :categoryId))
		        OR (:professionId IS NOT NULL AND pr.profession_id = :professionId)
		      )
		      AND (:skillId IS NULL OR ps.skill_id = :skillId)
		      AND (
		        :keyword IS NULL OR :keyword = '' OR
		        LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
		        LOWER(pr.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
		        LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
		        LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
		        LOWER(p.last_name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
		        LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
		      )
		    """, nativeQuery = true)
		List<Professional> searchProfessionals(
		    @Param("categoryId") Long categoryId,
		    @Param("professionId") Long professionId,
		    @Param("skillId") Long skillId,
		    @Param("keyword") String keyword
		);





}
