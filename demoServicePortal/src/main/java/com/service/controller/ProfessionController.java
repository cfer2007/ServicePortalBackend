package com.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.service.dto.IProfessionDTO;
import com.service.dto.ProfessionDTO;
import com.service.exception.ResourceNotFoundException;
import com.service.model.Profession;
import com.service.repository.ProfessionRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/profession")
public class ProfessionController {
	
	@Autowired
	private ProfessionRepository repo;
	
	@PostMapping("/add")
	public ResponseEntity<Profession> setProfession(@Valid @RequestBody Profession profession) {
		Profession p = repo.save(profession);
		return ResponseEntity.ok(p);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<List<IProfessionDTO>>getListProfessionsByCategory(@PathVariable Long id){
		List<IProfessionDTO> list = repo.findByCategoryId(id);
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/all")
	//@PreAuthorize("hasAuthority('ADMIN')")
	  public ResponseEntity<List<Profession>> getAllProfessions() {
		List<Profession> list = repo.findAll();
		return ResponseEntity.ok(list);
	}
	
	@PutMapping("/edit/{id}")
	public ResponseEntity<Long> editProfession(@PathVariable Long id, @RequestBody ProfessionDTO dto) {
		Profession existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Profession not found",1001));
		dto.updateEntity(existing);
		Profession updated = repo.save(existing);
		return ResponseEntity.ok(updated.getProfessionId());
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteProfession(@PathVariable("id") Long id) {
		repo.deleteById(id);			
		return ResponseEntity.ok("Profession deleted");	
	}
}

