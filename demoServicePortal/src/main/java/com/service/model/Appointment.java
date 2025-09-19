package com.service.model;

import com.service.enums.AppointmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Table(name="appointment")
@Entity(name="appointment")
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long appointmentId;
	
	@ManyToOne
    @JoinColumn(name = "clientId", referencedColumnName = "clientId",nullable = true)
	private Client client;
	
	@ManyToOne
    @JoinColumn(name = "professionalId", referencedColumnName = "professionalId",nullable = true)
	private Professional professional;
	
	@ManyToOne
    @JoinColumn(name = "professionalAvailabilityId", referencedColumnName = "professionalAvailabilityId",nullable = true)
	private ProfessionalAvailability professionalAvailability;
	
	@Column//(pattern = "dd/MM/yyyy HH:MM")
    private String appointmentDate;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private AppointmentStatus status;

	public Long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Professional getProfessional() {
		return professional;
	}

	public void setProfessional(Professional professional) {
		this.professional = professional;
	}

	public ProfessionalAvailability getProfessionalAvailability() {
		return professionalAvailability;
	}

	public void setProfessionalAvailability(ProfessionalAvailability professionalAvailability) {
		this.professionalAvailability = professionalAvailability;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public AppointmentStatus getStatus() {
		return status;
	}

	public void setStatus(AppointmentStatus status) {
		this.status = status;
	}
}
