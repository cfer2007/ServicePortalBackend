package com.service.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.service.model.Skill;
import com.service.dto.ProfessionalSkillDTO;
import com.service.model.Professional;
import com.service.model.ProfessionalSkill;
import com.service.repository.ProfessionalSkillRepository;

@RestController
@RequestMapping(value="/professional_skill",
produces = "application/json", 
method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ProfessionalSkillController {
	
	@Autowired
	private ProfessionalSkillRepository repo;
	
	/*@PostMapping("/add/list/{idProfessional}")
	public List<ProfessionalSkillDTO> setProfessionalSkillList(@PathVariable("idProfessional") Long id, @RequestBody List<Skill> listSkill) {
		
		for(Skill skill : listSkill) {
			
			Professional sp = new Professional();
			sp.setProfessionalId(id);
			ProfessionalSkill sk = new ProfessionalSkill();
			sk.setProfessional(sp);
			sk.setSkill(skill);
			
			try {
				repo.save(sk);
            } catch (Exception e) {
                System.out.println("duplicate key");
            }
			
		}
		
		return repo.findByProfessionalId(id);
	}*/
	
	@PostMapping("/add/list/{idProfessional}")
	public List<ProfessionalSkill> setProfessionalSkillList(@PathVariable("idProfessional") Long id, @RequestBody List<ProfessionalSkill> listSkill) {
		
		return repo.saveAll(listSkill);
	}
	
	@GetMapping("/get/{id}")
	public List<ProfessionalSkillDTO> getListByProfessional(@PathVariable("id") Long id){
		return repo.findByProfessionalId(id);
	}
	
	@DeleteMapping("/delete/{id}")
	public void deleteProfessionalSkill(@PathVariable("id") Long id) {
		repo.deleteById(id);
	}
	
}