package com.service.controller;

import com.service.enums.DocumentStatus;
import com.service.enums.DocumentType;
import com.service.enums.ProfileStatus;
import com.service.model.Professional;
import com.service.model.ProfessionalDocument;
import com.service.repository.ProfessionalDocumentRepository;
import com.service.repository.ProfessionalRepository;
import com.service.service.ProfessionalValidationService;

import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/professional_document")
@CrossOrigin(origins = {"http://localhost:3000"})
class LegacyProfessionalDocumentController {

    private final ProfessionalDocumentRepository documentRepo;
    private final ProfessionalRepository professionalRepo;
    private final ProfessionalValidationService validationService;

    LegacyProfessionalDocumentController(ProfessionalDocumentRepository documentRepo,
                                         ProfessionalRepository professionalRepo,
                                         ProfessionalValidationService validationService) {
        this.documentRepo = documentRepo;
        this.professionalRepo = professionalRepo;
        this.validationService = validationService;
    }

    @PostMapping(value = "/upload/{professionalId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    @PreAuthorize("hasAuthority('PROFESSIONAL')")
    public ResponseEntity<?> legacyUpload(
        @PathVariable Long professionalId,
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "type", required = false) DocumentType legacyType,
        @RequestParam(value = "docType", required = false) DocumentType docType
    ) {
        try {
            DocumentType effectiveType = (docType != null) ? docType : legacyType;
            if (effectiveType == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "type/docType es requerido"));
            }
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "file vacío o ausente"));
            }

            Professional professional = professionalRepo.findById(professionalId)
                .orElseThrow(() -> new NoSuchElementException("Professional " + professionalId + " not found"));

            String originalName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String ext = "";
            int dot = originalName.lastIndexOf('.');
            if (dot >= 0) ext = originalName.substring(dot);

            String storedName = UUID.randomUUID() + (ext.isBlank() ? "" : ext);
            Path uploadDir = Path.of("uploads", "professionals", String.valueOf(professionalId));

            Files.createDirectories(uploadDir);
            Path target = uploadDir.resolve(storedName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            String url = "/uploads/professionals/" + professionalId + "/" + storedName;

            // 🔹 Buscar si ya existe documento del mismo tipo
            Optional<ProfessionalDocument> existingOpt =
                documentRepo.findByProfessional_ProfessionalIdAndType(professionalId, effectiveType);

            ProfessionalDocument doc;
            if (existingOpt.isPresent()) {
                // actualizar documento existente
                doc = existingOpt.get();

                // borrar archivo previo si existe
                try {
                    String oldUrl = doc.getUrl();
                    if (oldUrl != null && oldUrl.startsWith("/uploads/")) {
                        Path oldPath = Path.of(oldUrl.substring(1)).normalize();
                        Path root = Path.of("uploads").toAbsolutePath().normalize();
                        if (oldPath.toAbsolutePath().startsWith(root)) {
                            Files.deleteIfExists(oldPath);
                        }
                    }
                } catch (Exception ignored) {}

            } else {
                // crear documento nuevo
                doc = new ProfessionalDocument();
                doc.setProfessional(professional);
                doc.setType(effectiveType);
            }

            doc.setStatus(DocumentStatus.PENDING);
            doc.setReadable(true);
            doc.setFileName(originalName);
            doc.setContentType(file.getContentType());
            doc.setSizeBytes(file.getSize());
            doc.setUrl(url);
            doc.setDate(Instant.now());
            doc.setStatusReason(null);

            ProfessionalDocument saved = documentRepo.save(doc);

            try {
                var auto = validationService.run(professionalId);
                if (auto.documentsComplete && auto.filesReadable) {
                    professional.setStatus(ProfileStatus.PENDING_REVIEW);
                    professionalRepo.save(professional);
                }
            } catch (Exception ignored) {}

            return ResponseEntity.ok(saved);

        } catch (NoSuchElementException notFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", notFound.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Error al subir documento (legacy)"));
        }
    }
    
    @GetMapping("/get/{professionalId}")
    public ResponseEntity<?> getByProfessional(@PathVariable Long professionalId) {
        try {
            List<ProfessionalDocument> docs = documentRepo.findByProfessional_ProfessionalId(professionalId);
            return ResponseEntity.ok(docs);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Error listando documentos"));
        }
    }
    
    @PutMapping(value = "/update/{documentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    @PreAuthorize("hasAuthority('PROFESSIONAL')")
    public ResponseEntity<?> update(
        @PathVariable Long documentId,
        @RequestParam("file") MultipartFile file,
        @RequestParam(value = "docType", required = false) DocumentType docType,
        @RequestParam(value = "type", required = false) DocumentType legacyType // compatibilidad
    ) {
        try {
            DocumentType effectiveType = (docType != null) ? docType : legacyType;
            if (effectiveType == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "type/docType es requerido"));
            }
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "file vacío o ausente"));
            }

            // Buscar documento existente por su ID
            ProfessionalDocument doc = documentRepo.findById(documentId)
                .orElseThrow(() -> new NoSuchElementException("Document " + documentId + " not found"));

            Professional professional = doc.getProfessional();
            Long professionalId = professional.getProfessionalId();

            // Guardar archivo nuevo
            String originalName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String ext = "";
            int dot = originalName.lastIndexOf('.');
            if (dot >= 0) ext = originalName.substring(dot);
            String storedName = UUID.randomUUID() + (ext.isBlank() ? "" : ext);

            Path uploadDir = Path.of("uploads", "professionals", String.valueOf(professionalId));
            Files.createDirectories(uploadDir);
            Path target = uploadDir.resolve(storedName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            String newUrl = "/uploads/professionals/" + professionalId + "/" + storedName;

            // Borrar archivo previo
            try {
                String oldUrl = doc.getUrl();
                if (oldUrl != null && oldUrl.startsWith("/uploads/")) {
                    Path oldPath = Path.of(oldUrl.substring(1)).normalize();
                    Path root = Path.of("uploads").toAbsolutePath().normalize();
                    if (oldPath.toAbsolutePath().startsWith(root)) {
                        Files.deleteIfExists(oldPath);
                    }
                }
            } catch (Exception ignored) {}

            // Actualizar el registro existente
            doc.setType(effectiveType); // por si acaso
            doc.setUrl(newUrl);
            doc.setFileName(originalName);
            doc.setContentType(file.getContentType());
            doc.setSizeBytes(file.getSize());
            doc.setDate(Instant.now());
            doc.setStatus(DocumentStatus.PENDING);   // vuelve a pendiente para revisión
            doc.setStatusReason(null);

            ProfessionalDocument saved = documentRepo.save(doc);

            // Importante: aquí NO tocamos el ProfileStatus

            return ResponseEntity.ok(saved);

        } catch (NoSuchElementException notFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", notFound.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("message", "Error actualizando documento", "error", ex.getClass().getSimpleName()));
        }
    }


}
