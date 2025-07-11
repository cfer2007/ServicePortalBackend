package com.service.dto;

import com.service.enums.Status;
import com.service.model.Appointment;

public class AppointmentDTO {

	private Status status;
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
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
