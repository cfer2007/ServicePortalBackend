package com.service.dto;

import com.service.enums.ReservationStatus;
import com.service.model.Appointment;

public class AppointmentDTO {

	private ReservationStatus status;
	
	public ReservationStatus getStatus() {
		return status;
	}

	public void setStatus(ReservationStatus status) {
		this.status = status;
	}
	/*
	public Appointment toEntity() {
		Appointment app = new Appointment();
		app.setStatus(this.status);
		return app;
	}*/
	
	public void updateEntity(Appointment entity) {
		entity.setStatus(status);
	}
}
