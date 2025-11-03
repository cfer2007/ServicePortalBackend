package com.service.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import com.service.enums.ReviewStatus;

@Entity
@Table(
    name = "review",
    uniqueConstraints = @UniqueConstraint(columnNames = {"appointment_id"})
)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    // ✅ Relación con la cita → garantiza reseña real
    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    // ✅ Cliente que hizo la reseña
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    // ✅ Profesional reseñado
    @ManyToOne
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;

    // ⭐ 1–5 estrellas
    @Column(nullable = false)
    private int rating;

    // comentario opcional
    @Column(length = 500)
    private String comment;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus status = ReviewStatus.VISIBLE; 
    
    @Column(length = 500)
    private String professionalReply;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column
    private LocalDateTime updatedAt;

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

	public Long getReviewId() {
		return reviewId;
	}

	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}

	public Appointment getAppointment() {
		return appointment;
	}

	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
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

	public ReviewStatus getStatus() {
		return status;
	}

	public void setStatus(ReviewStatus status) {
		this.status = status;
	}

	public String getProfessionalReply() {
		return professionalReply;
	}

	public void setProfessionalReply(String professionalReply) {
		this.professionalReply = professionalReply;
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
}