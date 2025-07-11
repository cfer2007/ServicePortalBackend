package com.service.address.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.address.dto.AddressDTO;
import com.service.address.model.Address;
import com.service.address.repository.AddressRepository;
import com.service.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/address")
public class AddressController {

	@Autowired
	private AddressRepository repo;
	
	@PostMapping("/add")
	public ResponseEntity<Address> setAddress(@RequestBody Address address){
		Address newAddress = repo.save(address);
		return ResponseEntity.ok(newAddress);
	}
	
	@GetMapping("/get/client/{id}")
	public ResponseEntity<List<Address>> getAddressByClientId(@PathVariable Long id){
		List<Address> list = repo.getAdressesByClientId(id);
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/get/professional/{id}")
	public ResponseEntity<List<Address>> getAddressByProfessionalId(@PathVariable Long id){
		List<Address> list = repo.getAddressesByProfessionalId(id);
		return ResponseEntity.ok(list);
	}
	
	@PutMapping("/edit/{id}")
	public ResponseEntity<Address> editAddress(@PathVariable Long id, @RequestBody AddressDTO dto){
		Address existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address "+id+" not found", 1003));
		dto.updateEntity(existing);
		Address updated = repo.save(existing);		
		return ResponseEntity.ok(updated);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteAddress(@PathVariable Long id){
		repo.deleteById(id);
		return ResponseEntity.ok("Address deleted");
	}
}
