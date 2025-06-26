package com.service.model;

import com.service.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Table(name="reservation")
@Entity(name="reservation")
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long reservationId;
	
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
    private String reservationDate;
	
	@Column
	private Status status;

	public Long getReservationId() {
		return reservationId;
	}

	public void setReservationId(Long reservationId) {
		this.reservationId = reservationId;
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

	public String getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(String reservationDate) {
		this.reservationDate = reservationDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
