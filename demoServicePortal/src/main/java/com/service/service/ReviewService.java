package com.service.service;

import com.service.enums.AppointmentStatus;
import com.service.enums.ReviewStatus;
import com.service.model.Appointment;
import com.service.model.Review;
import com.service.repository.ReviewRepository;
import com.service.repository.AppointmentRepository;
import com.service.dto.ReviewDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepo;
    private final AppointmentRepository appointmentRepo;

    public ReviewService(ReviewRepository reviewRepo, AppointmentRepository appointmentRepo) {
        this.reviewRepo = reviewRepo;
        this.appointmentRepo = appointmentRepo;
    }

    // ✅ Crear reseña desde DTO
    @Transactional
    public ReviewDTO create(ReviewDTO dto) {

        if (dto.getAppointmentId() == null || dto.getClientId() == null) {
            throw new IllegalArgumentException("appointmentId y clientId son requeridos");
        }

        Appointment appointment = appointmentRepo.findById(dto.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("La cita no existe"));

        if (!appointment.getClient().getClientId().equals(dto.getClientId())) {
            throw new IllegalArgumentException("Esta cita no pertenece al cliente");
        }

        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new IllegalArgumentException("Solo puedes reseñar citas completadas");
        }

        var existing = reviewRepo.findByAppointmentAppointmentId(dto.getAppointmentId());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Ya existe una reseña para esta cita");
        }

        if (dto.getProfessionalId() != null &&
                !appointment.getProfessional().getProfessionalId().equals(dto.getProfessionalId())) {
            throw new IllegalArgumentException("El profesional no coincide con la cita");
        }

        Review review = new Review();
        review.setAppointment(appointment);
        review.setClient(appointment.getClient());
        review.setProfessional(appointment.getProfessional());
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        review = reviewRepo.save(review);

        // ✅ Marca la cita como reseñada
        appointment.setHasReview(true);
        appointmentRepo.save(appointment);

        return ReviewDTO.from(review);
    }

    // ✅ Editar reseña
    @Transactional
    public ReviewDTO update(Long reviewId, ReviewDTO dto) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("La reseña no existe"));

        if (!review.getClient().getClientId().equals(dto.getClientId())) {
            throw new IllegalArgumentException("No puedes editar una reseña que no es tuya");
        }

        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        review = reviewRepo.save(review);

        // ✅ La cita sigue teniendo reseña
        Appointment appointment = review.getAppointment();
        if (!appointment.isHasReview()) {
            appointment.setHasReview(true);
            appointmentRepo.save(appointment);
        }

        return ReviewDTO.from(review);
    }

    // ✅ Eliminar reseña
    @Transactional
    public void delete(Long reviewId, Long clientId) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("La reseña no existe"));

        if (!review.getClient().getClientId().equals(clientId)) {
            throw new IllegalArgumentException("No puedes borrar una reseña que no es tuya");
        }

        Appointment appointment = review.getAppointment();

        reviewRepo.delete(review);

        // ✅ Volver a marcar la cita como sin reseña
        appointment.setHasReview(false);
        appointmentRepo.save(appointment);
    }

    // ✅ Obtener reseñas de un profesional
    public List<ReviewDTO> getByProfessional(Long professionalId) {
        return reviewRepo.findByProfessionalProfessionalIdOrderByCreatedAtDesc(professionalId)
                .stream()
                .map(ReviewDTO::from)
                .toList();
    }

    // ✅ Obtener reseñas hechas por un cliente
    public List<ReviewDTO> getByClient(Long clientId) {
        return reviewRepo.findByClientClientIdOrderByCreatedAtDesc(clientId)
                .stream()
                .map(ReviewDTO::from)
                .toList();
    }

    // ✅ Promedio y total como respuesta tipo DTO
    public Object getStats(Long professionalId) {
        Double avg = reviewRepo.getAverageRatingByProfessional(professionalId);
        Integer total = reviewRepo.getTotalReviewsByProfessional(professionalId);

        return new Object() {
            public final Double average = avg != null ? avg : 0.0;
            public final Integer count = total != null ? total : 0;
        };
    }
    
    public ReviewDTO getByAppointmentId(Long appointmentId) {
        Review review = reviewRepo.findByAppointmentAppointmentId(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("No existe reseña para esta cita"));
        return ReviewDTO.from(review);
    }

    @Transactional
    public void requestReviewDispute(Long reviewId, Long professionalId) {
        Review r = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("La reseña no existe"));

        if (!r.getProfessional().getProfessionalId().equals(professionalId)) {
            throw new IllegalArgumentException("No puedes solicitar revisión de una reseña que no es tuya");
        }

        r.setStatus(ReviewStatus.PENDING_REVIEW);
        reviewRepo.save(r);
    }

    @Transactional
    public void replyToReview(Long reviewId, Long professionalId, String reply) {
        Review r = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("La reseña no existe"));

        if (!r.getProfessional().getProfessionalId().equals(professionalId)) {
            throw new IllegalArgumentException("No puedes responder una reseña que no es tuya");
        }

        r.setProfessionalReply(reply);
        reviewRepo.save(r);
    }

}
