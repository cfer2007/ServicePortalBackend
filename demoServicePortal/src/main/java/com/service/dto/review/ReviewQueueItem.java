package com.service.dto.review;

import com.service.enums.ProfileStatus;

public class ReviewQueueItem {
	  public Long professionalId;
	  public String name, lastName, email, professionName;
	  public ProfileStatus status;         // mantenemos "status" aquí
	  public java.time.LocalDateTime submittedAt; // opcional
	}