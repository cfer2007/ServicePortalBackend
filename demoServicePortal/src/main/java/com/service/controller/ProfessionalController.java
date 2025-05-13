package com.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.service.dto.ProfessionalDTO;
import com.service.exception.ResourceNotFoundException;
import com.service.model.Professional;
import com.service.repository.ProfessionalRepository;

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
	
	@GetMapping("/get/{id}")
	public ResponseEntity<Professional> getProfessional(@PathVariable Long id){
		Professional p = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Professional "+id+" not found",1003));
		return ResponseEntity.ok(p);
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
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteProfessional(@PathVariable Long id) {
		repo.deleteById(id);
		return ResponseEntity.ok("Professional deleted");
	}
}