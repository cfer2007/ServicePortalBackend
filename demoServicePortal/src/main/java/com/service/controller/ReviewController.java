package com.service.controller;

//package: com.service.controller
import com.service.dto.review.*;
import com.service.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/review")
//@PreAuthorize("hasAnyRole('ADMIN','REVIEWER')")
public class ReviewController {

@Autowired ReviewService reviewService;

@GetMapping("/queue")
public Page<ReviewQueueItem> getQueue(@RequestParam(defaultValue="0") int page,
                                     @RequestParam(defaultValue="10") int size,
                                     @RequestParam(required=false) String q) {
 return reviewService.getQueue(page, size, q);
}

@GetMapping("/bundle/{professionalId}")
public ProfessionalReviewDTO getBundle(@PathVariable Long professionalId) {
 return reviewService.getBundle(professionalId);
}

@PostMapping("/decision/{professionalId}")
public void decide(@PathVariable Long professionalId,
                  @RequestBody ReviewDecisionRequest req,
                  Authentication auth) {
 reviewService.decide(professionalId, req, auth.getName());
}
}
