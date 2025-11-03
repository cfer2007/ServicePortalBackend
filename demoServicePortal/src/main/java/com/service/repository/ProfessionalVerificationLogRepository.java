package com.service.repository;

import com.service.model.ProfessionalVerificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessionalVerificationLogRepository extends JpaRepository<ProfessionalVerificationLog, Long> {
    List<ProfessionalVerificationLog> findByProfessional_ProfessionalIdOrderByDecidedAtDesc(Long professionalId);
}