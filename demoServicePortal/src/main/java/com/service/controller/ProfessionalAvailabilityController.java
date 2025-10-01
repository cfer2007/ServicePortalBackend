package com.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.dto.AvailabilityDTO;
import com.service.enums.AvailabilityStatus;
import com.service.exception.ResourceNotFoundException;
import com.service.model.Professional;
import com.service.model.ProfessionalAvailability;
import com.service.repository.ProfessionalAvailabilityRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@RestController
@RequestMapping(value="/professional_availability",
produces = "application/json", 
method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ProfessionalAvailabilityController {
	
	@Autowired
	private ProfessionalAvailabilityRepository repo;
	
	@PersistenceContext
	private EntityManager entityManager; 
	
	@PostMapping("/add/list")
	@PreAuthorize("hasAuthority('PROFESSIONAL')")
	public ResponseEntity<?> setList(@RequestBody List<AvailabilityDTO> list) {
	    List<ProfessionalAvailability> entities = list.stream().map(dto -> {
	        ProfessionalAvailability pa = new ProfessionalAvailability();
	        pa.setDay(dto.getDay());
	        pa.setStartTime(dto.getStartTime());
	        pa.setEndTime(dto.getEndTime());
	        pa.setStatus(dto.getStatus());

	        Professional professional = entityManager.getReference(Professional.class, dto.getProfessionalId());
	        pa.setProfessional(professional);

	        return pa;
	    }).toList();

	    repo.saveAll(entities);
	    return ResponseEntity.ok("Schedules successfully added");
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<List<ProfessionalAvailability>> getProfessionalAvailabilitiesByProfessional(@PathVariable Long id){
		List<ProfessionalAvailability> list = repo.findByProfesionalId(id);		
		return ResponseEntity.ok(list);
	}
	
	@PutMapping("/edit/status/{id}")
	@PreAuthorize("hasAuthority('PROFESSIONAL')")
	public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam AvailabilityStatus status) {
		System.out.println(status);
		ProfessionalAvailability existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Availability "+id+" not found",1003));
		existing.setStatus(status);
		return ResponseEntity.ok(repo.save(existing));
	}
}
