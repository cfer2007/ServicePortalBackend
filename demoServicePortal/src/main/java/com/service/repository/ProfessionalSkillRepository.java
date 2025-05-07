package com.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.service.dto.ProfessionalSkillDTO;
import com.service.model.ProfessionalSkill;

public interface ProfessionalSkillRepository extends JpaRepository<ProfessionalSkill, Long>{

	@Query(value = "select sk.professional_id professionalId, sk.professional_skill_id professionalSkillId, s.skill_id skillId, s.name\n"
			+ " from skill s, professional_skill sk \n"
			+ " where sk.professional_id =?1\n"
			+ " and s.skill_id=sk.skill_id", nativeQuery = true)
	List<ProfessionalSkillDTO> findByProfessionalId(@Param("id")Long id);	
}
