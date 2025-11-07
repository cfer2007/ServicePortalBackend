package com.service.dto;

import com.service.enums.AppointmentStatus;
import com.service.model.Appointment;
import com.service.model.ProfessionalAvailability;

public class AppointmentRequest {

    // ✅ para creación
    private Long clientId;
    private Long professionalId;
    private Long professionalAvailabilityId;
    private String appointmentDate;

    // ✅ para notificaciones
    private Long actorUserRoleId;       

    // ✅ para actualizar estado
    private AppointmentStatus status;

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public Long getProfessionalId() { return professionalId; }
    public void setProfessionalId(Long professionalId) { this.professionalId = professionalId; }

    public Long getProfessionalAvailabilityId() { return professionalAvailabilityId; }
    public void setProfessionalAvailabilityId(Long professionalAvailabilityId) {
        this.professionalAvailabilityId = professionalAvailabilityId;
    }

    public String getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }

    public Long getActorUserRoleId() { return actorUserRoleId; }
    public void setActorUserRoleId(Long actorUserRoleId) { this.actorUserRoleId = actorUserRoleId; }


    // ✅ Update general (fecha, disponibilidad, estado)
    public void updateEntity(Appointment entity) {
        if (this.appointmentDate != null) {
            entity.setAppointmentDate(this.appointmentDate);
        }

        if (this.professionalAvailabilityId != null) {
            ProfessionalAvailability pa = new ProfessionalAvailability();
            pa.setProfessionalAvailabilityId(this.professionalAvailabilityId);
            entity.setProfessionalAvailability(pa);
        }

        if (this.status != null) {
            entity.setStatus(this.status);
        }
    }

    // ✅ Solo actualizar estado
    public void updateStatus(Appointment entity) {
        if (this.status != null) {
            entity.setStatus(this.status);
        }
    }
}
