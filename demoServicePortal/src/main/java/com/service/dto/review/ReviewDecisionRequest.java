package com.service.dto.review;

import java.util.List;

import com.service.enums.DocumentType;

public class ReviewDecisionRequest {
	  public String decision;                    // APPROVED | REQUIRES_CHANGES | REJECTED
	  public String notes;
	  public List<DocumentType> requestedDocs; // opcional
	}