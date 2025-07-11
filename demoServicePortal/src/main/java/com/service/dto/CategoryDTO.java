package com.service.dto;

import com.service.model.Category;

import jakarta.validation.constraints.NotBlank;

public class CategoryDTO {

	@NotBlank(message = "Name is required")
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	/*
	public Category toEntity() {
		Category c = new Category();
		c.setName(this.name);
		return c;
	}*/
	
	public void updateEntity(Category entity) {
		entity.setName(name);
	}
}
