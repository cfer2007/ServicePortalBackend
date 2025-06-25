package com.service.dto;

import com.service.model.Profession;
import com.service.model.Professional;

public class ProfessionalDTO {

 private String name;

 private String lastName;
 
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

