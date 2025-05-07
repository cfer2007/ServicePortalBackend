package com.service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	public List<ProfessionalAvailability> setList(@RequestBody List<ProfessionalAvailability> list) {
		try {
			repo.saveAll(list);
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return repo.findAll();
	}
	
	@GetMapping("/get/{id}")
	public List<ProfessionalAvailability> getProfessionalAvailabilitiesByProfessional(@PathVariable("id") Long id){
		return repo.findByProfesionalId(id);
	}
	
	@DeleteMapping("/delete/{id}")
	public void deleteProfessionalAvailability(@PathVariable("id") Long id) {
		repo.deleteById(id);
	}
}
