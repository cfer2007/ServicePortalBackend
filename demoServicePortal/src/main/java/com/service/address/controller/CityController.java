package com.service.address.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.address.model.City;
import com.service.address.repository.CityRepository;

@RestController
@RequestMapping("/city")
public class CityController {

	@Autowired
	private CityRepository repo;
	
	@GetMapping("/get/{id}")
	public ResponseEntity<List<City>> getCitiesByRegion(@PathVariable Long id){
		List<City> list = repo.getCitiesByRegion(id);
		return ResponseEntity.ok(list);
	}
}
