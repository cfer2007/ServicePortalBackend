package com.service.dto;

import java.time.Instant;

import com.service.enums.DocumentStatus;
import com.service.enums.DocumentType;
import com.service.model.ProfessionalDocument;

public record ProfessionalDocumentDTO(
		Long id,
	    DocumentType type,
	    String url,
	    DocumentStatus status,
	    Instant date
	) {
	    public static ProfessionalDocumentDTO from(ProfessionalDocument doc) {
	        return new ProfessionalDocumentDTO(
	            doc.getProfessionalDocumentId(),
	            doc.getType(),
	            doc.getUrl(),
	            doc.getStatus(),
	            doc.getDate()
	        );
	    }
	}

