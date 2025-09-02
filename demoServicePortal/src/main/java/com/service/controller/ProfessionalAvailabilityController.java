package com.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.service.model.ProfessionalAvailability;
import com.service.repository.ProfessionalAvailabilityRepository;

@RestController
@RequestMapping(value="/professional_availability",
produces = "application/json", 
method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ProfessionalAvailabilityController {
	
	@Autowired
	private ProfessionalAvailabilityRepository repo;
	
	@PostMapping("/add/list")
	@PreAuthorize("hasAuthority('PROFESSIONAL')")
	public ResponseEntity<?> setList(@RequestBody List<ProfessionalAvailability> list) {
		repo.saveAll(list);
		return ResponseEntity.ok("Schedules successfully added");
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<List<ProfessionalAvailability>> getProfessionalAvailabilitiesByProfessional(@PathVariable Long id){
		List<ProfessionalAvailability> list = repo.findByProfesionalId(id);		
		return ResponseEntity.ok(list);
	}
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('PROFESSIONAL')")
	public ResponseEntity<?> deleteProfessionalAvailability(@PathVariable("id") Long id) {
		repo.deleteById(id);
		return ResponseEntity.ok("Schedules deleted");
	}
}
