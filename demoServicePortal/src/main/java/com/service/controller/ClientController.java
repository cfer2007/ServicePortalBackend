package com.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.dto.ClientDTO;
import com.service.exception.ResourceNotFoundException;
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
	@PutMapping("/edit/{id}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> editClient(@PathVariable Long id, @RequestBody ClientDTO dto){
		Client existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client not found",1001));
		dto.updateEntity(existing);
		Client updated = repo.save(existing);
		return ResponseEntity.ok(updated.getClientId());
	}
}
