package com.service.dto;

import com.service.model.Profession;
import com.service.model.Skill;

import jakarta.validation.constraints.NotBlank;

public class SkillDTO {

	@NotBlank(message = "Name is required")
	private String name;
	
	//@NotBlank(message = "Profession is required")
	private Long professionId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getProfessionId() {
		return professionId;
	}

	public void setProfessionId(Long professionId) {
		this.professionId = professionId;
	}
	
	public Skill toEntity() {
		Profession profession = new Profession();
	    profession.setProfessionId(this.professionId);
	    
		Skill s = new Skill();
		s.setName(this.name);
		s.setProfession(profession);
		
		return s;
	}
	
	public void updateEntity(Skill entity) {
		entity.setName(this.name);
	}
}
