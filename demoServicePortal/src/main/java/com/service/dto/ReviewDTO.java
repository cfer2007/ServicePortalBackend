package com.service.dto;

import java.time.LocalDateTime;

import com.service.enums.ReviewStatus;
import com.service.model.Appointment;
import com.service.model.Client;
import com.service.model.Professional;
import com.service.model.Review;

public class ReviewDTO {

    private Long reviewId;
    private Long appointmentId;
    private Long clientId;
    private Long professionalId;
    private int rating;
    private String comment;
    private String professionalReply;
    private ReviewStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // --- Getters & Setters ---

    public Long getReviewId() {
        return reviewId;
    }
    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }
    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getClientId() {
        return clientId;
    }
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getProfessionalId() {
        return professionalId;
    }
    public void setProfessionalId(Long professionalId) {
        this.professionalId = professionalId;
    }

    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getProfessionalReply() {
		return professionalReply;
	}
	public void setProfessionalReply(String professionalReply) {
		this.professionalReply = professionalReply;
	}
	public ReviewStatus getStatus() {
		return status;
	}
	public void setStatus(ReviewStatus status) {
		this.status = status;
	}
	// --- Convertidor: entidad → DTO ---
    public static ReviewDTO from(Review r) {
        ReviewDTO dto = new ReviewDTO();
        dto.setReviewId(r.getReviewId());

        if (r.getAppointment() != null) {
            dto.setAppointmentId(r.getAppointment().getAppointmentId());
        }
        if (r.getClient() != null) {
            dto.setClientId(r.getClient().getClientId());
        }
        if (r.getProfessional() != null) {
            dto.setProfessionalId(r.getProfessional().getProfessionalId());
        }

        dto.setProfessionalReply(r.getProfessionalReply());        // ✅
        dto.setStatus(r.getStatus());  // ✅ si es enum
        dto.setRating(r.getRating());
        dto.setComment(r.getComment());
        dto.setCreatedAt(r.getCreatedAt());
        dto.setUpdatedAt(r.getUpdatedAt());

        return dto;
    }

    // --- Crear entidad desde DTO ---
    public Review toEntity(Appointment appt, Client client, Professional professional) {
        Review r = new Review();
        r.setAppointment(appt);
        r.setClient(client);
        r.setProfessional(professional);
        r.setRating(this.rating);
        r.setComment(this.comment);
        return r;
    }

    // --- Actualizar entidad existente ---
    public void updateEntity(Review r) {
        r.setRating(this.rating);
        r.setComment(this.comment);
        r.setStatus(this.status);
    }
}
