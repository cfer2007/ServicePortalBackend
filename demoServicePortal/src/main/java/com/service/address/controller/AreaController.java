package com.service.address.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.address.model.Area;
import com.service.address.repository.AreaRepository;

@RestController
@RequestMapping("/area")
public class AreaController {

	@Autowired
	private AreaRepository repo;
	
	@GetMapping("/get/{id}")
	public ResponseEntity<List<Area>> getAreasByCity(@PathVariable Long id){
		List<Area> list = repo.getAreasByCity(id);
		return ResponseEntity.ok(list);
	}
}
