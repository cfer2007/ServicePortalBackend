package com.service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Table(name="professional",uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
@Entity(name="professional")
public class Professional {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long professionalId;
	
	@Column
	private String name;
	
	@Column
	private String lastName;
	
	@Column
	private String email;
	
	@ManyToOne
    @JoinColumn(name = "professionId", referencedColumnName = "professionId",nullable = true)
	private Profession profession;

	public Long getProfessionalId() {
		return professionalId;
	}

	public void setProfessionalId(Long professionalId) {
		this.professionalId = professionalId;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}	

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Profession getProfession() {
		return profession;
	}

	public void setProfession(Profession profession) {
		this.profession = profession;
	}
	
}
