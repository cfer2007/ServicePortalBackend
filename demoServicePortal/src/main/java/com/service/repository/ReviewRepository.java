package com.service.repository;

import com.service.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProfessionalProfessionalIdOrderByCreatedAtDesc(Long professionalId);
    List<Review> findByClientClientIdOrderByCreatedAtDesc(Long clientId);
    Optional<Review> findByAppointmentAppointmentId(Long appointmentId);
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.professional.professionalId = :professionalId")
    Double getAverageRatingByProfessional(Long professionalId);
    @Query("SELECT COUNT(r) FROM Review r WHERE r.professional.professionalId = :professionalId")
    int getTotalReviewsByProfessional(Long professionalId);
}
