package com.service.controller;

import com.service.dto.ReplyDTO;
import com.service.dto.ReviewDTO;
import com.service.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewService service;

    public ReviewController(ReviewService service) {
        this.service = service;
    }

    // ✅ Crear reseña
    @PostMapping
    public ResponseEntity<ReviewDTO> create(@RequestBody ReviewDTO dto) {
        ReviewDTO saved = service.create(dto);
        return ResponseEntity.ok(saved);
    }

    // ✅ Editar reseña
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> update(
            @PathVariable Long reviewId,
            @RequestBody ReviewDTO dto) {

        ReviewDTO updated = service.update(reviewId, dto);
        return ResponseEntity.ok(updated);
    }

    // ✅ Eliminar reseña
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> delete(
            @PathVariable Long reviewId,
            @RequestParam Long clientId) {

        service.delete(reviewId, clientId);
        return ResponseEntity.ok("Reseña eliminada");
    }

    // ✅ Obtener reseñas de un profesional
    @GetMapping("/professional/{professionalId}")
    public ResponseEntity<List<ReviewDTO>> getByProfessional(@PathVariable Long professionalId) {
        List<ReviewDTO> list = service.getByProfessional(professionalId);
        return ResponseEntity.ok(list);
    }

    // ✅ Obtener reseñas hechas por un cliente
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ReviewDTO>> getByClient(@PathVariable Long clientId) {
        List<ReviewDTO> list = service.getByClient(clientId);
        return ResponseEntity.ok(list);
    }

    // ✅ Estadísticas (promedio y total)
    @GetMapping("/stats/{professionalId}")
    public ResponseEntity<?> getStats(@PathVariable Long professionalId) {
        return ResponseEntity.ok(service.getStats(professionalId));
    }
    
    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<ReviewDTO> getByAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(service.getByAppointmentId(appointmentId));
    }

    @PostMapping("/dispute/{reviewId}")
    public ResponseEntity<?> disputeReview(
            @PathVariable Long reviewId,
            @RequestParam Long professionalId) {

        service.requestReviewDispute(reviewId, professionalId);
        return ResponseEntity.ok("Solicitud enviada para revisión");
    }

    @PostMapping("/reply/{reviewId}")
    public ResponseEntity<?> replyReview(
            @PathVariable Long reviewId,
            @RequestParam Long professionalId,
            @RequestBody ReplyDTO dto) {

        service.replyToReview(reviewId, professionalId, dto.reply);
        return ResponseEntity.ok("Respuesta agregada");
    }
    
    @GetMapping("/disputes")
    public ResponseEntity<List<ReviewDTO>> getAllPendingDisputes() {
    	System.out.println(service.getAllPendingDisputes().size());
        return ResponseEntity.ok(service.getAllPendingDisputes());
    }

    @PostMapping("/admin/resolve/{reviewId}")
    public ResponseEntity<?> resolveReview(
            @PathVariable Long reviewId,
            @RequestParam boolean remove) {

        service.resolveReview(reviewId, remove);
        return ResponseEntity.ok("Revisión procesada");
    }

}
