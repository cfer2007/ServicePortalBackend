// src/main/java/com/service/controller/ProfessionalDocumentController.java
package com.service.controller;

import com.service.dto.ProfessionalDocumentDTO;
import com.service.enums.DocumentStatus;
import com.service.enums.DocumentType;
import com.service.model.*;
import com.service.repository.ProfessionalDocumentRepository;
import com.service.repository.ProfessionalRepository;
import com.service.service.FileStorageService;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//imports típicos omitidos por brevedad
@RestController
@RequestMapping("/professional_document")
public class ProfessionalDocumentController {

 private final ProfessionalDocumentRepository docRepo;
 private final ProfessionalRepository professionalRepo;
 private final FileStorageService storage;

 public ProfessionalDocumentController(
     ProfessionalDocumentRepository docRepo,
     ProfessionalRepository professionalRepo,
     FileStorageService storage
 ) {
     this.docRepo = docRepo;
     this.professionalRepo = professionalRepo;
     this.storage = storage;
 }

 @PostMapping("/upload/{professionalId}")
 @Transactional
 public ResponseEntity<ProfessionalDocumentDTO> upload(
         @PathVariable Long professionalId,
         @RequestParam("file") MultipartFile file,
         @RequestParam("type") DocumentType type
 ) throws Exception {

     String url = storage.store(professionalId, file, "identity");

     // referencia administrada (no crear new Professional)
     Professional profRef = professionalRepo.getReferenceById(professionalId);

     ProfessionalDocument doc = docRepo
         .findByProfessional_ProfessionalIdAndType(professionalId, type)
         .orElseGet(ProfessionalDocument::new);

     doc.setProfessional(profRef);
     doc.setType(type);
     doc.setUrl(url);
     doc.setStatus(DocumentStatus.PENDING);
     doc.setDate(Instant.now());

     ProfessionalDocument saved = docRepo.save(doc);
     return ResponseEntity.ok(ProfessionalDocumentDTO.from(saved));
 }

 @GetMapping("/list/{professionalId}")
 public ResponseEntity<List<ProfessionalDocumentDTO>> list(@PathVariable Long professionalId) {
     List<ProfessionalDocumentDTO> out = docRepo
         .findByProfessional_ProfessionalId(professionalId)
         .stream()
         .map(ProfessionalDocumentDTO::from)
         .toList();

     return ResponseEntity.ok(out);
 }
}
