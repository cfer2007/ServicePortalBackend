package com.service.dto;

import com.service.model.Professional;
import com.service.model.ProfessionalSkill;
import com.service.model.Skill;

public class ProfessionalSkillDTO {
	
	private Long profesionalId;
	
	private Long skillId;

	public Long getProfesionalId() {
		return profesionalId;
	}

	public void setProfesionalId(Long profesionalId) {
		this.profesionalId = profesionalId;
	}

	public Long getSkillId() {
		return skillId;
	}

	public void setSkillId(Long skillId) {
		this.skillId = skillId;
	}
	
	public ProfessionalSkill toEntity() {
		Professional p = new Professional();
		p.setProfessionalId(this.profesionalId);
		
		Skill s = new Skill();
		s.setSkillId(this.skillId);
		
		ProfessionalSkill ps = new ProfessionalSkill();
		ps.setProfessional(p);
		ps.setSkill(s);
		
		return ps;
	}
}
