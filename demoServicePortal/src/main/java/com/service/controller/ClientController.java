package com.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.model.Client;
import com.service.repository.ClientRepository;

@RestController
@RequestMapping("/client")
public class ClientController {

	@Autowired
	private ClientRepository repo;
	
	@GetMapping("/get/{email}")
	public ResponseEntity<Client> getClientByEmail(@PathVariable String email){
		Client p = repo.findByEmail(email);
		return ResponseEntity.ok(p);
	}
}
