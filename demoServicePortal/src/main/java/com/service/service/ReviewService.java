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

    // 1) Cargar profesional y todos sus documentos (evita depender de 'current')
    Professional p = professionalRepo.findById(professionalId)
        .orElseThrow(() -> new IllegalArgumentException("Professional " + professionalId + " not found"));

    List<com.service.model.ProfessionalDocument> docs =
        documentRepo.findByProfessional_ProfessionalId(professionalId);

    // 2) Aplicar la decisión
    if ("APPROVED".equals(decision)) {
      // Aprueba PENDING / REQUIRES_CHANGES y limpia motivo
      for (com.service.model.ProfessionalDocument d : docs) {
        com.service.enums.DocumentStatus st = d.getStatus();
        if (st == com.service.enums.DocumentStatus.PENDING ||
            st == com.service.enums.DocumentStatus.REQUIRES_CHANGES) {
          d.setStatus(com.service.enums.DocumentStatus.APPROVED);
          d.setStatusReason(null);
        }
      }
      documentRepo.saveAll(docs);

      p.setStatus(com.service.enums.ProfileStatus.APPROVED); // o ACTIVE si así lo manejas
      // Si tienes un campo reviewerNotes en Professional, puedes limpiarlo aquí:
      // p.setReviewerNotes(null);
      professionalRepo.save(p);
    }
    else if ("REJECTED".equals(decision)) {
      p.setStatus(com.service.enums.ProfileStatus.REJECTED);
      // Si tienes reviewerNotes y quieres mostrar el mensaje general:
      // p.setReviewerNotes(req.notes);
      professionalRepo.save(p);
    }
    else if ("REQUIRES_CHANGES".equals(decision)) {
      // Marcar como REQUIRES_CHANGES únicamente los tipos solicitados
      if (req.requestedDocs != null) {
        for (com.service.enums.DocumentType type : req.requestedDocs) {
          if (type == null) continue;

          // Buscar el doc más reciente por tipo (por campo 'date')
          com.service.model.ProfessionalDocument latest = null;
          for (com.service.model.ProfessionalDocument d : docs) {
            if (type.equals(d.getType())) {
              if (latest == null ||
                  (d.getDate() != null && (latest.getDate() == null || d.getDate().isAfter(latest.getDate())))) {
                latest = d;
              }
            }
          }

          if (latest != null) {
            latest.setStatus(com.service.enums.DocumentStatus.REQUIRES_CHANGES);
            // Guardamos el motivo general en statusReason (no hay motivo por-doc en tu DTO)
            latest.setStatusReason(nullIfBlank(req.notes));
            documentRepo.save(latest);
          } else {
            // Si no existe doc de ese tipo aún, crea un registro mínimo observado
            com.service.model.ProfessionalDocument d = new com.service.model.ProfessionalDocument();
            d.setProfessional(p);
            d.setType(type);
            d.setStatus(com.service.enums.DocumentStatus.REQUIRES_CHANGES);
            d.setStatusReason(nullIfBlank(req.notes));
            d.setDate(java.time.Instant.now());
            // d.setRequired(true); d.setReadable(false); // opcional
            documentRepo.save(d);
            docs.add(d); // mantenerlo en memoria por si se repite el tipo
          }
        }
      }

      p.setStatus(com.service.enums.ProfileStatus.REQUIRES_CHANGES);
      // Si tienes reviewerNotes en Professional, puedes persistir el mensaje general:
      // p.setReviewerNotes(req.notes);
      professionalRepo.save(p);
    }
    else {
      throw new IllegalArgumentException("Decisión inválida: " + req.decision);
    }

    // 3) Log de auditoría (conserva tu implementación)
    com.service.model.ProfessionalReviewLog log = new com.service.model.ProfessionalReviewLog();
    log.setProfessional(p);
    log.setReviewerEmail(reviewerEmail);
    // mapea el estado final del perfil según decisión
    com.service.enums.ProfileStatus logged =
        "APPROVED".equals(decision) ? com.service.enums.ProfileStatus.APPROVED :
        "REJECTED".equals(decision) ? com.service.enums.ProfileStatus.REJECTED :
                                      com.service.enums.ProfileStatus.REQUIRES_CHANGES;
    log.setNewStatus(logged);
    log.setNotes(req.notes);
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
