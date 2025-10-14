package com.service.dto;

import com.service.model.Category;
import com.service.model.Skill;

import jakarta.validation.constraints.NotBlank;

public class SkillDTO {

	@NotBlank(message = "Name is required")
	private String name;
	
	private Long categoryId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Skill toEntity() {
		Category category = new Category();
	    category.setCategoryId(this.categoryId);
	    
		Skill s = new Skill();
		s.setName(this.name);
		s.setCategory(category);
		
		return s;
	}
	
	public void updateEntity(Skill entity) {
		entity.setName(this.name);
	}
}
