package com.service.controller;

//package: com.service.controller
import com.service.dto.review.*;
import com.service.service.ProfessionalVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/verification")
//@PreAuthorize("hasAnyRole('ADMIN','REVIEWER')")
public class ProfessionalVerificationController {

	@Autowired ProfessionalVerificationService reviewService;
	
	@GetMapping("/queue")
	@PreAuthorize("hasAuthority('ADMIN')")
	public Page<ReviewQueueItem> getQueue(@RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size, @RequestParam(required=false) String q) {
		return reviewService.getQueue(page, size, q);
	}
	
	@GetMapping("/bundle/{professionalId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ProfessionalReviewDTO getBundle(@PathVariable Long professionalId) {
		return reviewService.getBundle(professionalId);
	}
	
	@PostMapping("/decision/{professionalId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Void> decide(@PathVariable Long professionalId, @RequestBody ReviewDecisionRequest req, Authentication auth) {
		//reviewService.decide(professionalId, req, auth.getName());
		final String reviewer = (auth != null && auth.getName() != null) ? auth.getName() : "system";
	    reviewService.decide(professionalId, req, reviewer);
	    return ResponseEntity.noContent().build();
	}
}
