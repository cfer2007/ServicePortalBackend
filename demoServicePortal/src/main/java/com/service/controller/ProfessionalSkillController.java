package com.service.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.service.dto.IProfessionalSkillDTO;
import com.service.model.ProfessionalSkill;
import com.service.repository.ProfessionalSkillRepository;

@RestController
@RequestMapping(value="/professional_skill")
public class ProfessionalSkillController {
	
	@Autowired
	private ProfessionalSkillRepository repo;
	
	@PostMapping("/add/list")
	@PreAuthorize("hasAuthority('PROFESSIONAL')")
	public ResponseEntity<?> setProfessionalSkillList( @RequestBody List<ProfessionalSkill> listSkill) {
		
		repo.saveAll(listSkill);
		return ResponseEntity.ok("Skills successfully added");
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<List<IProfessionalSkillDTO>> getListByProfessional(@PathVariable Long id){
		List<IProfessionalSkillDTO> list = repo.findByProfessionalId(id);
		return ResponseEntity.ok(list);
	}
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('PROFESSIONAL')")
	public ResponseEntity<?> deleteProfessionalSkill(@PathVariable Long id) {
		repo.deleteById(id);
		return ResponseEntity.ok("Skill deleted");
	}
	
}