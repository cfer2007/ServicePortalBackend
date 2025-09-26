package com.service.dto;

import com.service.enums.ProfileStatus;
import com.service.model.Profession;
import com.service.model.Professional;

public class ProfessionalDTO {
	
	private Long professionalId;
	
	private String name;
	
	private String lastName;
	 
	private String phone;
	 
	private String title;
		
	private String about;
		
	private String experienceYears;
		
	private float rate;
	 
	private String rate_type;
	 
	private Long professionId;
	 
	private String modality;
	 
	private ProfileStatus status;
	
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

	public Professional toEntity() {
		
	    Professional p = new Professional();
	    p.setName(this.name);
	    //p.setLastName(this.lastName);
	    return p;
	}
	
	public void updateEntity(Professional entity) {
		Profession profession = new Profession();
	    profession.setProfessionId(this.professionId);		
	    entity.setName(this.name);
	    entity.setLastName(this.lastName);
	    entity.setPhone(this.phone);
	    entity.setProfession(profession);	
	    entity.setTitle(this.title);
	    entity.setAbout(this.about);
	    entity.setExperienceYears(this.experienceYears);
	    entity.setRate_type(this.rate_type);
	    entity.setRate(this.rate);
	    //entity.setStatus(this.status);
	    entity.setModality(this.modality);
	}
	
	public void updateModality(Professional entity) {
		entity.setModality(this.modality);
	}
	
	public static ProfessionalDTO from(Professional p) {
	    ProfessionalDTO dto = new ProfessionalDTO();

	    // Identificadores y datos base
	    dto.setProfessionalId(p.getProfessionalId());
	    dto.setName(p.getName());
	    dto.setLastName(p.getLastName());
	    dto.setPhone(p.getPhone());
	    dto.setTitle(p.getTitle());
	    dto.setAbout(p.getAbout());
	    dto.setExperienceYears(p.getExperienceYears());
	    dto.setRate_type(p.getRate_type());   // ⚠️ tu entidad usa rate_type con guión bajo
	    dto.setRate(p.getRate());
	    dto.setModality(p.getModality());
	    dto.setStatus(p.getStatus());         // en tu entidad el getter es getStatus()

	    // Profesión (solo IDs/nombre para evitar LAZY)
	    if (p.getProfession() != null) {	    	
	        dto.setProfessionId(p.getProfession().getProfessionId());
	        //dto.setProfessionName(p.getProfession().getName()); // si no lo necesitas, quítalo
	    }
	    return dto;
	}
}

