package com.service.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.service.dto.ISkillDTO;
import com.service.dto.SkillDTO;
import com.service.exception.ResourceNotFoundException;
import com.service.model.Skill;
import com.service.repository.SkillRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/skill")
public class SkillController {
	
	@Autowired
	private SkillRepository repo;
		
	@PostMapping("/add")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Skill> setSkill(@Valid @RequestBody SkillDTO dto) {
		Skill newSkill = repo.save(dto.toEntity());		
		return ResponseEntity.ok(newSkill);
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<List<ISkillDTO>> getListSkillsByProfession(@PathVariable Long id){
		List<ISkillDTO> list = repo.findByProfessionId(id);
		return ResponseEntity.ok(list);
	}
	
	@PutMapping("/edit/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Long> editSkill(@PathVariable Long id,@RequestBody SkillDTO dto) {
		Skill existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Skill "+dto.getName()+" not found",1002));
		dto.updateEntity(existing);
		Skill updated = repo.save(existing);		
		return ResponseEntity.ok(updated.getSkillId());
	}
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<?> deleteSkill(@PathVariable Long id) {
		repo.deleteById(id);
		return ResponseEntity.ok("Skill deleted");
	}
}