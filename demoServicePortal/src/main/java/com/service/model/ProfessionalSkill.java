package com.service.model;

import jakarta.persistence.*;

@Table(name="professional_skill",
	uniqueConstraints = {@UniqueConstraint(columnNames = {"professionalId", "skillId"})}
		)
@Entity(name="professional_skill")
public class ProfessionalSkill {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long professionalSkillId;
	
	@ManyToOne
    @JoinColumn(name = "professionalId", referencedColumnName = "professionalId", nullable = false)
	private Professional professional;
	
	@ManyToOne
    @JoinColumn(name = "skillId", referencedColumnName = "skillId", nullable = false)
	private Skill skill;

	public Long getProfessionalSkillId() {
		return professionalSkillId;
	}

	public void setProfessionalSkillId(Long professionalSkillId) {
		this.professionalSkillId = professionalSkillId;
	}	

	public Professional getProfessional() {
		return professional;
	}

	public void setProfessional(Professional professional) {
		this.professional = professional;
	}

	public Skill getSkill() {
		return skill;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}
}
