package com.service.dto.review;

import com.service.enums.DocumentStatus;
import com.service.enums.DocumentType;
import com.service.enums.ProfileStatus;

public class ProfessionalReviewDTO {
	  public Long professionalId;
	  public String name, lastName, email, phone, professionName;
	  public ProfileStatus profileStatus;

	  public AutoValidationDTO autoValidation;
	  public java.util.List<DocumentDTO> documents;
	  public java.util.List<AddressDTO> addresses;      // opcional
	  public java.util.List<ReviewLogItemDTO> history;

	  public static class DocumentDTO {
	    public Long id;
	    public DocumentType type;
	    public DocumentStatus status;
	    public boolean required;
	    public boolean readable;
	    public String fileName, contentType, url;
	    public Long sizeBytes;      // <— usa tu nuevo campo
	  }
	  public static class AddressDTO { public Long id; public String region, city, label; }
	  public static class ReviewLogItemDTO { public String reviewerEmail, decision, notes; public java.time.LocalDateTime decidedAt; }
	  public static class AutoValidationDTO { public boolean documentsComplete, filesReadable, nameMatchesId; public java.util.List<String> errors; }
	}
