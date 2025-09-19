package com.service.dto;

import com.service.enums.AppointmentStatus;
import com.service.model.Appointment;

public class AppointmentDTO {

	private AppointmentStatus status;
	
	public AppointmentStatus getStatus() {
		return status;
	}

	public void setStatus(AppointmentStatus status) {
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
