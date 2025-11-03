package com.service.service;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.service.dto.review.ProfessionalReviewDTO;
import com.service.dto.review.ReviewDecisionRequest;
import com.service.dto.review.ReviewQueueItem;
import com.service.enums.DocumentStatus;
import com.service.enums.ProfileStatus;
import com.service.model.Professional;
import com.service.model.ProfessionalDocument;
import com.service.model.ProfessionalVerificationLog;
import com.service.repository.ProfessionalDocumentRepository;
import com.service.repository.ProfessionalRepository;
import com.service.repository.ProfessionalVerificationLogRepository;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class ProfessionalVerificationService {

  private final ProfessionalRepository professionalRepo;
  private final ProfessionalDocumentRepository documentRepo;
  private final ProfessionalVerificationLogRepository logRepo;
  private final ProfessionalValidationService validationService;

  public ProfessionalVerificationService(ProfessionalRepository professionalRepo,
                       ProfessionalDocumentRepository documentRepo,
                       ProfessionalVerificationLogRepository logRepo,
                       ProfessionalValidationService validationService) {
    this.professionalRepo = professionalRepo;
    this.documentRepo = documentRepo;
    this.logRepo = logRepo;
    this.validationService = validationService;
  }

  public Page<ReviewQueueItem> getQueue(int page, int size, String q) {
    var statuses = List.of(ProfileStatus.PENDING_VERIFICATION, ProfileStatus.REQUIRES_CHANGES);
    var p = professionalRepo.searchQueue(statuses, q, PageRequest.of(page, size, Sort.by("professionalId").descending()));
    return p.map(this::toQueueItem);
  }

  private ReviewQueueItem toQueueItem(Professional prof) {
    var it = new ReviewQueueItem();
    it.professionalId = prof.getProfessionalId();
    it.name = prof.getName();
    it.lastName = prof.getLastName();
    it.email = (prof.getUser() != null) ? prof.getUser().getEmail() : null;
    it.professionName = (prof.getProfession() != null) ? prof.getProfession().getName() : null;
    it.status = prof.getStatus();
    return it;
  }

  public ProfessionalReviewDTO getBundle(Long professionalId) {
    var p = professionalRepo.findById(professionalId).orElseThrow();

    var dto = new ProfessionalReviewDTO();
    dto.professionalId = p.getProfessionalId();
    dto.name = p.getName();
    dto.lastName = p.getLastName();
    dto.email = (p.getUser() != null) ? p.getUser().getEmail() : null;
    dto.phone = p.getPhone();
    dto.professionName = (p.getProfession() != null) ? p.getProfession().getName() : null;
    dto.profileStatus = p.getStatus();

    dto.documents = documentRepo.findByProfessional_ProfessionalId(professionalId).stream().map(d -> {
        var di = new ProfessionalReviewDTO.DocumentDTO();
        di.id = d.getProfessionalDocumentId();
        di.type = d.getType();
        di.status = d.getStatus();
        di.fileName   =  d.getFileName();
        di.contentType=  d.getContentType();
        di.url = d.getUrl(); 
        return di;
      }).toList();

    dto.history = logRepo.findByProfessional_ProfessionalIdOrderByDecidedAtDesc(professionalId).stream().map(l -> {
        var h = new ProfessionalReviewDTO.ReviewLogItemDTO();
        h.reviewerEmail = l.getReviewerEmail();
        h.decision = l.getNewStatus().name();
        h.notes = l.getNotes();
        h.decidedAt = l.getDecidedAt();
        return h;
      }).toList();

    dto.autoValidation = validationService.run(professionalId);
    return dto;
  }
  @Transactional
  public void decide(Long professionalId, ReviewDecisionRequest req, String reviewerEmail) {
    if (req == null || req.decision == null) {
      throw new IllegalArgumentException("Decisión requerida");
    }
    final String decision = req.decision.trim().toUpperCase();
    Professional p = professionalRepo.findById(professionalId).orElseThrow(() -> new IllegalArgumentException("Professional " + professionalId + " not found"));

    List<ProfessionalDocument> docs = documentRepo.findByProfessional_ProfessionalId(professionalId);

    switch (decision) {
      case "APPROVED" -> {
        for (var d : docs) {
          var st = d.getStatus();
          if (st == DocumentStatus.PENDING ||
              st == DocumentStatus.REQUIRES_CHANGES) {
            d.setStatus(DocumentStatus.APPROVED);
          }
        }
        documentRepo.saveAll(docs);
        p.setStatus(ProfileStatus.APPROVED); // o ACTIVE si así lo manejas
        professionalRepo.save(p);
      }

      case "REJECTED" -> {
        p.setStatus(ProfileStatus.REJECTED);
        professionalRepo.save(p);
      }

      case "REQUIRES_CHANGES" -> {
        if (req.items == null || req.items.isEmpty()) {
          throw new IllegalArgumentException("Debes especificar al menos un documento con cambios.");
        }
        Map<Long, ProfessionalDocument> byId = new java.util.HashMap<>();
        for (var d : docs) {
          byId.put(d.getProfessionalDocumentId(), d); 
        }

        for (var item : req.items) {
          if (item.documentId == null) {
            throw new IllegalArgumentException("Falta documentId en un item de cambios.");
          }
          String comment = nullIfBlank(item.comment);
          if (comment == null) {
            throw new IllegalArgumentException("El documento " + item.documentId + " requiere comentario.");
          }

          var doc = byId.get(item.documentId);
          if (doc == null) {
            var found = documentRepo.findById(item.documentId)
                .orElseThrow(() -> new IllegalArgumentException("Documento " + item.documentId + " no existe"));
            if (!found.getProfessional().getProfessionalId().equals(professionalId)) {
              throw new IllegalArgumentException("Documento " + item.documentId + " no pertenece al profesional " + professionalId);
            }
            doc = found;
          }

          doc.setStatus(DocumentStatus.REQUIRES_CHANGES);
          documentRepo.save(doc);
        }

        p.setStatus(ProfileStatus.REQUIRES_CHANGES);
        professionalRepo.save(p);
      }
      default -> throw new IllegalArgumentException("Decisión inválida: " + req.decision);
    }

    var log = new ProfessionalVerificationLog();
    log.setProfessional(p);
    log.setReviewerEmail(reviewerEmail);
    var logged =
        "APPROVED".equals(decision) ? ProfileStatus.APPROVED :
        "REJECTED".equals(decision) ? ProfileStatus.REJECTED :
                                      ProfileStatus.REQUIRES_CHANGES;
    log.setNewStatus(logged);
    log.setNotes(req.notes); // mensaje general (opcional)
    log.setDecidedAt(java.time.LocalDateTime.now());
    logRepo.save(log);
  }

  private static String nullIfBlank(String s) {
    if (s == null) return null;
    String t = s.trim();
    return t.isEmpty() ? null : t;
  }


}
