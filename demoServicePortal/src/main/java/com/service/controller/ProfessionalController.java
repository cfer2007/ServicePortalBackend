package com.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<Professional> setProfessional(@Valid @RequestBody ProfessionalDTO dto) {
		Professional newProfessional = repo.save(dto.toEntity());
	    return ResponseEntity.ok(newProfessional);
	}
	/*
	@GetMapping("/get/{id}")
	public ResponseEntity<Professional> getProfessional(@PathVariable Long id){
		Professional p = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Professional "+id+" not found",1003));
		return ResponseEntity.ok(p);
	}
	*/
	
	@GetMapping("/get/{email}")
	public ResponseEntity<Professional> getProfessionalByEmail(@PathVariable String email){
	    Professional p = repo.findByEmail(email);
	    if (p == null) {
	        throw new ResourceNotFoundException("Professional with email "+email+" not found",1003);
	    }
	    return ResponseEntity.ok(p);
	}
	
	@GetMapping("/getList/{id}")
	public ResponseEntity<List<Professional>> getProfessionalBySkill(@PathVariable Long id){
		List<Professional> list = repo.findProfessionalsBySkill(id);
		
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/all")
	public  ResponseEntity<List<Professional>> getAllProfessionalList(){
		List<Professional> list = repo.findAll();		
		return ResponseEntity.ok(list);
	}
	
	@PutMapping("/edit/{id}")
	public ResponseEntity<Professional> editProfessional(@PathVariable Long id, @RequestBody ProfessionalDTO dto) {
		Professional existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Professional "+id+" not found",1003));
		dto.updateEntity(existing);
		Professional updated = repo.save(existing);
		return ResponseEntity.ok(updated);
	}
	
	@PutMapping("/edit/modality/{id}")
	public ResponseEntity<Professional> editModalityProfessional(@PathVariable Long id, @RequestBody ProfessionalDTO dto) {
		Professional existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Professional "+id+" not found",1003));
		dto.updateModality(existing);
		return ResponseEntity.ok(repo.save(existing));
	}
	
	@PutMapping("/edit/status/{id}")
	@Transactional
	public ResponseEntity<Professional> updateStatus(@PathVariable Long id, @RequestParam(name = "status") ProfileStatus status) {
	    System.out.println("updateStatus id=" + id + " status=" + status);
	    Professional existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Professional "+id+" not found",1003));
	    existing.setStatus(status);
	    return ResponseEntity.ok(repo.save(existing));
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteProfessional(@PathVariable Long id) {
		repo.deleteById(id);
		return ResponseEntity.ok("Professional deleted");
	}
}