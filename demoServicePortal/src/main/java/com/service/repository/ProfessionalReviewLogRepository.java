package com.service.repository;

import com.service.model.ProfessionalReviewLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessionalReviewLogRepository extends JpaRepository<ProfessionalReviewLog, Long> {
    List<ProfessionalReviewLog> findByProfessional_ProfessionalIdOrderByDecidedAtDesc(Long professionalId);
}