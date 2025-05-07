package com.service.dto;

import com.service.model.Profession;

import jakarta.validation.constraints.NotBlank;

public class ProfessionDTO {
	
	@NotBlank(message = "Name is required")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Profession toEntity() {
		Profession p = new Profession();
		p.setName(this.name);
		return p;
	}
	
	public void updateEntity(Profession entity) {
		entity.setName(name);
	}
}
