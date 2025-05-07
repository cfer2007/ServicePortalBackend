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

@Table(name="professional_availability",
uniqueConstraints = {@UniqueConstraint(columnNames = {"professional_id", "day", "start_time", "end_time"})})
@Entity(name="professional_availability")
public class ProfessionalAvailability {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long professionalAvailabilityId;
	
	@ManyToOne
    @JoinColumn(name = "professionalId", referencedColumnName = "professionalId", nullable = false)
	private Professional professional;
	
	@Column
	private String day;
	
	@Column
	private String startTime;
	
	@Column
	private String endTime;

	public Long getProfessionalAvailabilityId() {
		return professionalAvailabilityId;
	}

	public void setProfessionalAvailabilityId(Long professionalAvailabilityId) {
		this.professionalAvailabilityId = professionalAvailabilityId;
	}

	public Professional getProfessional() {
		return professional;
	}

	public void setProfessional(Professional professional) {
		this.professional = professional;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
