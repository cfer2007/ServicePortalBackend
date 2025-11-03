package com.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.service.dto.ProfessionalDTO;
import com.service.enums.ProfileStatus;
import com.service.exception.ResourceNotFoundException;
import com.service.model.Professional;
import com.service.repository.ProfessionalRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.*;

@RestController
@RequestMapping("/professional")
public class ProfessionalController {

	@Autowired
	private ProfessionalRepository repo;
	
	@PostMapping("/add")
	@PreAuthorize("hasAuthority('PROFESSIONAL')")
	public ResponseEntity<Professional> setProfessional(@Valid @RequestBody ProfessionalDTO dto) {
		Professional newProfessional = repo.save(dto.toEntity());
	    return ResponseEntity.ok(newProfessional);
	}
	
	@GetMapping("/get/{email}")
	@PreAuthorize("hasAuthority('PROFESSIONAL')")
	public ResponseEntity<Professional> getProfessionalByEmail(@PathVariable String email){
	    Professional p = repo.findByEmail(email);
	    if (p == null) {
	        throw new ResourceNotFoundException("Professional with email "+email+" not found",1003);
	    }
	    return ResponseEntity.ok(p);
	}
	
	@GetMapping("/all")
	@PreAuthorize("hasAuthority('USER')")
	public  ResponseEntity<List<Professional>> getAllProfessionalList(){
		List<Professional> list = repo.findAll();		
		return ResponseEntity.ok(list);
	}
	
	@PutMapping("/edit/{id}")
	@PreAuthorize("hasAuthority('PROFESSIONAL')")
	public ResponseEntity<Professional> editProfessional(@PathVariable Long id, @RequestBody ProfessionalDTO dto) {
		Professional existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Professional "+id+" not found",1003));
		dto.updateEntity(existing);
		Professional updated = repo.save(existing);
		return ResponseEntity.ok(updated);
	}
	
	@PutMapping("/edit/modality/{id}")
	@PreAuthorize("hasAuthority('PROFESSIONAL')")
	public ResponseEntity<Professional> editModalityProfessional(@PathVariable Long id, @RequestBody ProfessionalDTO dto) {		
		Professional existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Professional "+id+" not found",1003));
		dto.updateModality(existing);
		return ResponseEntity.ok(repo.save(existing));
	}
	
	@PutMapping("/edit/status/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN','PROFESSIONAL')")
	@Transactional
	public ResponseEntity<Professional> updateStatus(@PathVariable Long id, @RequestParam(name = "status") ProfileStatus status) {
	    Professional existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Professional "+id+" not found",1003));
	    existing.setStatus(status);
	    return ResponseEntity.ok(repo.save(existing));
	}
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> deleteProfessional(@PathVariable Long id) {
		repo.deleteById(id);
		return ResponseEntity.ok("Professional deleted");
	}
	
	@GetMapping("/search")
    public List<Professional> searchProfessionals(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long professionId,
            @RequestParam(required = false) Long skillId,
            @RequestParam(required = false) String keyword) {
		System.out.println(categoryId);
		System.out.println(professionId);
		System.out.println(skillId);
		System.out.println(keyword);
        return repo.searchProfessionals(categoryId, professionId, skillId, keyword);
    }

}