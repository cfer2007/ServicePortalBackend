package com.service.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.service.enums.DocumentType;
import com.service.model.ProfessionalDocument;

public interface ProfessionalDocumentRepository extends JpaRepository<ProfessionalDocument, Long>{
	List<ProfessionalDocument> findByProfessional_ProfessionalId(Long professionalId);	
	Optional<ProfessionalDocument> findByProfessional_ProfessionalIdAndType(Long professionalId, DocumentType type);
}
