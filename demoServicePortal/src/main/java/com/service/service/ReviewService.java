package com.service.service;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.service.dto.review.ProfessionalReviewDTO;
import com.service.dto.review.ReviewDecisionRequest;
import com.service.dto.review.ReviewQueueItem;
import com.service.enums.ProfileStatus;
import com.service.model.Professional;
import com.service.model.ProfessionalReviewLog;
import com.service.repository.ProfessionalDocumentRepository;
import com.service.repository.ProfessionalRepository;
import com.service.repository.ProfessionalReviewLogRepository;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class ReviewService {

  private final ProfessionalRepository professionalRepo;
  private final ProfessionalDocumentRepository documentRepo;
  private final ProfessionalReviewLogRepository logRepo;
  private final ProfessionalValidationService validationService;

  public ReviewService(ProfessionalRepository professionalRepo,
                       ProfessionalDocumentRepository documentRepo,
                       ProfessionalReviewLogRepository logRepo,
                       ProfessionalValidationService validationService) {
    this.professionalRepo = professionalRepo;
    this.documentRepo = documentRepo;
    this.logRepo = logRepo;
    this.validationService = validationService;
  }

  // --- Queue ---
  public Page<ReviewQueueItem> getQueue(int page, int size, String q) {
    var statuses = List.of(ProfileStatus.PENDING_REVIEW, ProfileStatus.REQUIRES_CHANGES);
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
    // it.submittedAt = prof.getUpdatedAt() != null ? prof.getUpdatedAt() : prof.getCreatedAt();
    return it;
  }

  // --- Bundle ---
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

    dto.documents = documentRepo.findByProfessional_ProfessionalId(professionalId)
      .stream().map(d -> {
        var di = new ProfessionalReviewDTO.DocumentDTO();

        // NOTE: ajusta este getter al nombre REAL de tu @Id en ProfessionalDocument
        // si tu id es "id": di.id = d.getId();
        // si es "professionalDocumentId": déjalo como está
        di.id = d.getProfessionalDocumentId();

        di.type = d.getType();
        di.status = d.getStatus();
        di.statusReason = d.getStatusReason();
        di.required = d.isRequired();
        di.readable = d.isReadable();

        // Estos 3 requieren campos en la entidad; comenta si aún no existen
        di.fileName   = /* puede ser null */ d.getFileName();
        di.contentType= /* puede ser null */ d.getContentType();
        di.sizeBytes  = /* puede ser null */ d.getSizeBytes();

        di.url = d.getUrl(); // puede ser null; el frontend ya maneja fallback
        return di;
      }).toList();

    dto.history = logRepo.findByProfessional_ProfessionalIdOrderByDecidedAtDesc(professionalId)
      .stream().map(l -> {
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

  // --- Decisión ---
  /*@Transactional
  public void decide(Long professionalId, ReviewDecisionRequest req, String reviewerEmail) {
    var p = professionalRepo.findById(professionalId).orElseThrow();

    var newStatus = switch (req.decision) {
      case "APPROVED" -> ProfileStatus.APPROVED;
      case "REQUIRES_CHANGES" -> ProfileStatus.REQUIRES_CHANGES;
      case "REJECTED" -> ProfileStatus.REJECTED;
      default -> throw new IllegalArgumentException("Decisión inválida");
    };

    p.setStatus(newStatus);
    professionalRepo.save(p);

    var log = new ProfessionalReviewLog();
    log.setProfessional(p);
    log.setReviewerEmail(reviewerEmail);
    log.setNewStatus(newStatus);
    log.setNotes(req.notes);
    log.setDecidedAt(java.time.LocalDateTime.now());
    logRepo.save(log);

    // TODO opcional: usar req.requestedDocs para marcar tipos como requeridos de nuevo
  }*/
  @Transactional
  public void decide(Long professionalId, ReviewDecisionRequest req, String reviewerEmail) {
    if (req == null || req.decision == null) {
      throw new IllegalArgumentException("Decisión requerida");
    }

    final String decision = req.decision.trim().toUpperCase();

    // 1) Cargar profesional y sus documentos
    Professional p = professionalRepo.findById(professionalId)
        .orElseThrow(() -> new IllegalArgumentException("Professional " + professionalId + " not found"));

    List<com.service.model.ProfessionalDocument> docs =
        documentRepo.findByProfessional_ProfessionalId(professionalId);

    switch (decision) {
      case "APPROVED" -> {
        // Aprueba PENDING / REQUIRES_CHANGES y limpia motivo
        for (var d : docs) {
          var st = d.getStatus();
          if (st == com.service.enums.DocumentStatus.PENDING ||
              st == com.service.enums.DocumentStatus.REQUIRES_CHANGES) {
            d.setStatus(com.service.enums.DocumentStatus.APPROVED);
            d.setStatusReason(null);
          }
        }
        documentRepo.saveAll(docs);

        p.setStatus(com.service.enums.ProfileStatus.APPROVED); // o ACTIVE si así lo manejas
        professionalRepo.save(p);
      }

      case "REJECTED" -> {
        p.setStatus(com.service.enums.ProfileStatus.REJECTED);
        professionalRepo.save(p);
      }

      case "REQUIRES_CHANGES" -> {
        // ✅ NUEVO: por documento (items)
        if (req.items == null || req.items.isEmpty()) {
          throw new IllegalArgumentException("Debes especificar al menos un documento con cambios.");
        }

        // Mapear por id para búsqueda rápida.
        // IMPORTANTE: ajusta el getter del ID si en tu entidad no es getProfessionalDocumentId().
        java.util.Map<Long, com.service.model.ProfessionalDocument> byId = new java.util.HashMap<>();
        for (var d : docs) {
          byId.put(d.getProfessionalDocumentId(), d); // <-- usa el nombre real de tu @Id
          // Si tu id es "getId()", cambia a: byId.put(d.getId(), d);
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
            // Fallback seguro: buscar por ID y validar pertenencia
            var found = documentRepo.findById(item.documentId)
                .orElseThrow(() -> new IllegalArgumentException("Documento " + item.documentId + " no existe"));
            if (!found.getProfessional().getProfessionalId().equals(professionalId)) {
              throw new IllegalArgumentException("Documento " + item.documentId + " no pertenece al profesional " + professionalId);
            }
            doc = found;
          }

          doc.setStatus(com.service.enums.DocumentStatus.REQUIRES_CHANGES);
          doc.setStatusReason(comment);
          // Si quieres, también puedes marcar readable=false cuando se piden cambios por legibilidad
          // doc.setReadable(Boolean.TRUE.equals(doc.isReadable()) ? doc.isReadable() : false);
          documentRepo.save(doc);
        }

        p.setStatus(com.service.enums.ProfileStatus.REQUIRES_CHANGES);
        professionalRepo.save(p);
      }

      default -> throw new IllegalArgumentException("Decisión inválida: " + req.decision);
    }

    // 3) Log de auditoría
    var log = new ProfessionalReviewLog();
    log.setProfessional(p);
    log.setReviewerEmail(reviewerEmail);
    var logged =
        "APPROVED".equals(decision) ? com.service.enums.ProfileStatus.APPROVED :
        "REJECTED".equals(decision) ? com.service.enums.ProfileStatus.REJECTED :
                                      com.service.enums.ProfileStatus.REQUIRES_CHANGES;
    log.setNewStatus(logged);
    log.setNotes(req.notes); // mensaje general (opcional)
    log.setDecidedAt(java.time.LocalDateTime.now());
    logRepo.save(log);
  }

  // helper
  private static String nullIfBlank(String s) {
    if (s == null) return null;
    String t = s.trim();
    return t.isEmpty() ? null : t;
  }


}
