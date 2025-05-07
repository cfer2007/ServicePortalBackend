package com.service.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.service.dto.ISkillDTO;
import com.service.model.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long>{

	@Query(value = "select skill_id as skillId, name from skill where profession_id=?1", nativeQuery = true)
	List<ISkillDTO> findByProfessionId(@Param("id")Long id);	
	
}
