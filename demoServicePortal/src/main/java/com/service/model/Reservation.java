package com.service.model;

import java.sql.Date;

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
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long reservationId;
	
	@ManyToOne
    @JoinColumn(name = "professionalId", referencedColumnName = "professionalId",nullable = false)
	private Professional professional;

	@ManyToOne
    @JoinColumn(name = "customerId", referencedColumnName = "customerId",nullable = false)
	private Customer customerId;
	
	@Column
	private String estado;
	
	@Column
	public Date reservationDate;
	
	@Column Date creationDate;

	public Long getReservationId() {
		return reservationId;
	}

	public void setReservationId(Long reservationId) {
		this.reservationId = reservationId;
	}

	public Professional getProfessional() {
		return professional;
	}

	public void setProfessional(Professional professional) {
		this.professional = professional;
	}

	public Customer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Customer customerId) {
		this.customerId = customerId;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(Date reservationDate) {
		this.reservationDate = reservationDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
