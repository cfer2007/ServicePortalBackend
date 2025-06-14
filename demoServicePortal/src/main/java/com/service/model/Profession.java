package com.service.model;

import jakarta.persistence.*;

@Table(name="profession")
@Entity(name="profession")
public class Profession {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long professionId;
	
	@Column
	private String name;
	
	@ManyToOne
    @JoinColumn(name = "categoryId", referencedColumnName = "categoryId",nullable = false)
	private Category category;	

	public Long getProfessionId() {
		return professionId;
	}

	public void setProfessionId(Long professionId) {
		this.professionId = professionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
