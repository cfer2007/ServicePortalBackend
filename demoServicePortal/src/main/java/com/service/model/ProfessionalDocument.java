package com.service.model;

import com.service.enums.DocumentType;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.service.enums.DocumentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

//@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Entity
@Table(name = "professional_document")
public class ProfessionalDocument {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long professionalDocumentId;
	
	@ManyToOne
    @JoinColumn(name = "professionalId", referencedColumnName = "professionalId",nullable = true)
	//@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "professional_id", nullable = false)
    //@JsonIgnore   // <- evita que Jackson intente serializar el proxy LAZY
	private Professional professional;
	
	@Column
	@Enumerated(EnumType.STRING)
	private DocumentType type;
	
	@Column
	private String url;
	
	@Column
	@Enumerated(EnumType.STRING)
	private DocumentStatus status;
	
	@Column
	private Instant date = Instant.now();

	public Long getProfessionalDocumentId() {
		return professionalDocumentId;
	}

	public void setProfessionalDocumentId(Long professionalDocumentId) {
		this.professionalDocumentId = professionalDocumentId;
	}

	public Professional getProfessional() {
		return professional;
	}

	public void setProfessional(Professional professional) {
		this.professional = professional;
	}

	public DocumentType getType() {
		return type;
	}

	public void setType(DocumentType type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public DocumentStatus getStatus() {
		return status;
	}

	public void setStatus(DocumentStatus status) {
		this.status = status;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}
}
