package com.service.enums;


public enum ReservationStatus {
	PENDING,       // Pendiente
	CONFIRMED,     // Confirmada
	RESCHEDULED,   // Reprogramada
	CANCELLED_BY_CLIENT,
	CANCELLED_BY_PROFESSIONAL,
	IN_PROGRESS,   // En curso
	COMPLETED,     // Finalizada
	NO_SHOW,       // No asistió	
}
