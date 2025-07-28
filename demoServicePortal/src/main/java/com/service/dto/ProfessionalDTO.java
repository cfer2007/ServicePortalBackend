package com.service.dto;

import com.service.model.Profession;
import com.service.model.Professional;

public class ProfessionalDTO {

 private String name;

 private String lastName;
 
 private String title;
	
 private String about;
	
 private String experienceYears;
	
 private float rate;
 
 private String rate_type;
 
 private Long professionId;
 
 private String modality;

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

	public Long getProfessionId() {
		return professionId;
	}
	
	public void setProfessionId(Long professionId) {
		this.professionId = professionId;
	}
	
	public String getModality() {
		return modality;
	}

	public void setModality(String modality) {
		this.modality = modality;
	}

	public Professional toEntity() {
		
	    Professional p = new Professional();
	    p.setName(this.name);
	    p.setLastName(this.lastName);
	    return p;
	}
	
	public void updateEntity(Professional entity) {
		Profession profession = new Profession();
	    profession.setProfessionId(this.professionId);		
	    entity.setName(this.name);
	    entity.setLastName(this.lastName);
	    entity.setProfession(profession);	
	    entity.setTitle(this.title);
	    entity.setAbout(this.about);
	    entity.setExperienceYears(this.experienceYears);
	    entity.setRate_type(this.rate_type);
	    entity.setRate(this.rate);
	}
	
	public void updateModality(Professional entity) {
		entity.setModality(this.modality);
	}
}

