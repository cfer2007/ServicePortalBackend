package com.service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name="skill")
@Entity(name="skill")
public class Skill {
	
	public Skill() {}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long skillId;
	
	@Column
	private String name;
	
	@ManyToOne
    @JoinColumn(name = "professionId", referencedColumnName = "professionId",nullable = false)
	private Profession profession;
	
	public Long getSkillId() {
		return skillId;
	}

	public void setSkillId(Long skillId) {
		this.skillId = skillId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Profession getProfession() {
		return profession;
	}

	public void setProfession(Profession profession) {
		this.profession = profession;
	}
}
