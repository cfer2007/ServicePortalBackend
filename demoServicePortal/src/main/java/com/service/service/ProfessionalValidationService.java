package com.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.dto.review.ProfessionalReviewDTO;
import com.service.enums.DocumentType;
import com.service.repository.ProfessionalDocumentRepository;

@Service
public class ProfessionalValidationService {

  @Autowired 
  ProfessionalDocumentRepository docRepo;

  private static final java.util.Set<DocumentType> REQUIRED = java.util.Set.of(
      DocumentType.DPI_FRONT, DocumentType.DPI_BACK, DocumentType.SELFIE,
      DocumentType.POLICE_RECORD, DocumentType.PENAL_RECORD
  );

  public ProfessionalReviewDTO.AutoValidationDTO run(Long professionalId) {
    var res = new ProfessionalReviewDTO.AutoValidationDTO();
    res.errors = new java.util.ArrayList<>();

    var docs = docRepo.findByProfessional_ProfessionalId(professionalId);
    var present = new java.util.HashSet<DocumentType>();
    boolean allReadable = true;

    for (var d : docs) {
      present.add(d.getType());
      if (!d.isReadable()) allReadable = false;
    }

    boolean missing = REQUIRED.stream().anyMatch(req -> !present.contains(req));
    res.documentsComplete = !missing;
    if (missing) res.errors.add("Faltan documentos requeridos.");

    res.filesReadable = allReadable;
    if (!allReadable) res.errors.add("Hay archivos no legibles.");

    res.nameMatchesId = true; // implementar si haces OCR
    return res;
  }
}
