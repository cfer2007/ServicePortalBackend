package com.service.model;

import com.service.auth.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name="professional")
@Entity(name="professional")
public class Professional {
	
	public Professional() {}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long professionalId;
	
	@Column
	private String name;
	
	@Column
	private String lastName;
	
	@ManyToOne
    @JoinColumn(name = "professionId", referencedColumnName = "professionId",nullable = true)
	private Profession profession;
	
	@ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
	private User user;

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

	public Profession getProfession() {
		return profession;
	}

	public void setProfession(Profession profession) {
		this.profession = profession;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
