package com.service.repository;

import com.service.enums.ReviewStatus;
import com.service.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProfessionalProfessionalIdOrderByCreatedAtDesc(Long professionalId);
    
 // ✅ SOLO VISIBLES (para clientes)
    List<Review> findByProfessionalProfessionalIdAndStatusOrderByCreatedAtDesc(
            Long professionalId,
            ReviewStatus status
    );
    
    List<Review> findByClientClientIdOrderByCreatedAtDesc(Long clientId);
    
    Optional<Review> findByAppointmentAppointmentId(Long appointmentId);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.professional.professionalId = :professionalId and status = VISIBLE")
    Double getAverageRatingByProfessional(Long professionalId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.professional.professionalId = :professionalId and status = VISIBLE")
    int getTotalReviewsByProfessional(Long professionalId);
    
    List<Review> findByStatus(ReviewStatus status);

}
