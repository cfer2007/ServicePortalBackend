package com.service.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.service.enums.ProfileStatus;

@Entity @Table(name = "professional_review_log")
public class ProfessionalReviewLog {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long ProfessionalReviewLogId;

  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "professional_id", nullable = false)
  private Professional professional;

  @Column(nullable = false)
  private String reviewerEmail;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ProfileStatus newStatus;         // estado al que quedó el perfil

  @Column(length = 2000)
  private String notes;

  @Column(nullable = false)
  private LocalDateTime decidedAt;

  public Long getProfessionalReviewLogId() {
	return ProfessionalReviewLogId;
  }

  public void setProfessionalReviewLogId(Long professionalReviewLogId) {
	ProfessionalReviewLogId = professionalReviewLogId;
  }

  public Professional getProfessional() {
	return professional;
  }

  public void setProfessional(Professional professional) {
	this.professional = professional;
  }

  public String getReviewerEmail() {
	 return reviewerEmail;
  }

  public void setReviewerEmail(String reviewerEmail) {
	this.reviewerEmail = reviewerEmail;
  }

  public ProfileStatus getNewStatus() {
	return newStatus;
  }

  public void setNewStatus(ProfileStatus newStatus) {
	this.newStatus = newStatus;
  }

  public String getNotes() {
	return notes;
  }

  public void setNotes(String notes) {
	this.notes = notes;
  }

  public LocalDateTime getDecidedAt() {
	return decidedAt;
  }

  public void setDecidedAt(LocalDateTime decidedAt) {
	this.decidedAt = decidedAt;
  }  
}