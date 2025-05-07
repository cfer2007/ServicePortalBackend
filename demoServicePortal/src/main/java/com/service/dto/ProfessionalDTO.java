package com.service.dto;

import com.service.model.Profession;
import com.service.model.Professional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ProfessionalDTO {

 private String name;

 private String lastName;

 @NotBlank(message = "Email is required")
 @Email(message = "Email must be valid")
 private String email;
 
 private Long professionId;

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
	
	public Long getProfessionId() {
		return professionId;
	}
	
	public void setProfessionId(Long professionId) {
		this.professionId = professionId;
	}
	
	public Professional toEntity() {
		
	    Professional p = new Professional();
	    p.setName(this.name);
	    p.setLastName(this.lastName);
	    p.setEmail(this.email);	
	    return p;
	}
	
	public void updateEntity(Professional entity) {
		Profession profession = new Profession();
	    profession.setProfessionId(this.professionId);		
	    entity.setName(this.name);
	    entity.setLastName(this.lastName);
	    entity.setProfession(profession);	
	}
}

