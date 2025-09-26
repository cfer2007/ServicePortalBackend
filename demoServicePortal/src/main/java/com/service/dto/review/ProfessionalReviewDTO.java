package com.service.dto.review;

import java.time.LocalDateTime;
import java.util.List;

import com.service.enums.DocumentStatus;
import com.service.enums.DocumentType;
import com.service.enums.ProfileStatus;

public class ProfessionalReviewDTO {
	  public Long professionalId;
	  public String name, lastName, email, phone, professionName;
	  public ProfileStatus profileStatus;

	  public AutoValidationDTO autoValidation;
	  public List<DocumentDTO> documents;
	  public List<AddressDTO> addresses;      // opcional
	  public List<ReviewLogItemDTO> history;

	  public static class DocumentDTO {
	    public Long id;
	    public DocumentType type;
	    public DocumentStatus status;
	    public String statusReason;
	    public boolean required;
	    public boolean readable;
	    public String fileName, contentType, url;
	    public Long sizeBytes;      // <— usa tu nuevo campo
	  }
	  public static class AddressDTO { public Long id; public String region, city, label; }
	  public static class ReviewLogItemDTO { public String reviewerEmail, decision, notes; public LocalDateTime decidedAt; }
	  public static class AutoValidationDTO { public boolean documentsComplete, filesReadable, nameMatchesId; public List<String> errors; }
	}
