package com.service.model;

import com.service.enums.DocumentType;

import java.time.Instant;

import com.service.enums.DocumentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "professional_document")
public class ProfessionalDocument {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long professionalDocumentId;
	
	@ManyToOne
    @JoinColumn(name = "professionalId", referencedColumnName = "professionalId",nullable = true)
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
	
	
	private Long sizeBytes;	
	public Long getSizeBytes() { return sizeBytes; }
	public void setSizeBytes(Long sizeBytes) { this.sizeBytes = sizeBytes; }
	
	private boolean required;
	private boolean readable;	
	public boolean isRequired() { return required; }
	public void setRequired(boolean required) { this.required = required; }
	public boolean isReadable() { return readable; }
	public void setReadable(boolean readable) { this.readable = readable; }
	
	@Column
	private String fileName;

	@Column
	private String contentType;
	
	@Column(name = "status_reason")
    private String statusReason;

	public String getFileName() { return fileName; }
	public void setFileName(String fileName) { this.fileName = fileName; }

	public String getContentType() { return contentType; }
	public void setContentType(String contentType) { this.contentType = contentType; }
	
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
	public String getStatusReason() {
		return statusReason;
	}
	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}
}
