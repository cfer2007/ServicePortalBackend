package com.service.model;

import com.service.auth.model.User;
import com.service.enums.ProfileStatus;

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
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long professionalId;
	
	@Column
	private String name;
	
	@Column
	private String lastName;
	
	@Column
	private String phone;
	
	@Column
	private String title;
	
	@Column
	private String about;
	
	@Column
	private String experienceYears;
	
	@Column
	private float rate;
	
	@Column
	private String rate_type;
	
	@Column
	private String modality;
	
	@Column
	private ProfileStatus status;
	
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getExperienceYears() {
		return experienceYears;
	}

	public void setExperienceYears(String experienceYears) {
		this.experienceYears = experienceYears;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public String getRate_type() {
		return rate_type;
	}

	public void setRate_type(String rate_type) {
		this.rate_type = rate_type;
	}	

	public String getModality() {
		return modality;
	}

	public void setModality(String modality) {
		this.modality = modality;
	}

	public ProfileStatus getStatus() {
		return status;
	}

	public void setStatus(ProfileStatus status) {
		this.status = status;
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

	public Professional setUser(User user) {
		this.user = user;
		return this;
	}
}
