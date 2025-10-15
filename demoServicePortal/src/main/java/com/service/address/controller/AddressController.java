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

import com.service.address.dto.ProfessionalAddressDTO;
import com.service.address.dto.ClientAddressDTO;
import com.service.address.dto.IAddressDTO;
import com.service.address.model.Address;
import com.service.address.model.AddressClient;
import com.service.address.model.AddressProfessional;
import com.service.address.repository.AddressClientRepository;
import com.service.address.repository.AddressProfessionalRepository;
import com.service.address.repository.AddressRepository;
import com.service.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/address")
public class AddressController {

	@Autowired
	private AddressRepository repo;
	
	@Autowired
	private AddressClientRepository clientRepo;
	
	@Autowired
	private AddressProfessionalRepository professionalRepo;
	
	@PostMapping("/add/professional")
	@Transactional
	public ResponseEntity<Address> setProfessionlAddress(@RequestBody ProfessionalAddressDTO dto){
		Address newAddress = repo.save(dto.toEntity());
		AddressProfessional link = dto.toAddressProfessional(newAddress);
		if(!professionalRepo.existsByAddress_AddressIdAndProfessional_ProfessionalId(newAddress.getAddressId(), dto.getProfessionalId())) {
			professionalRepo.save(link);
		}		
		return ResponseEntity.ok(newAddress);
	}
	
	@PostMapping("/add/client")
	@Transactional
	public ResponseEntity<Address> setClientAddress(@RequestBody ClientAddressDTO dto) {
	    Address newAddress = repo.save(dto.toEntity());
	    AddressClient link = dto.toAddressClient(newAddress);
	    if (!clientRepo.existsByAddress_AddressIdAndClient_ClientId(newAddress.getAddressId(), dto.getClientId())) {
	        clientRepo.save(link);
	    }
	    return ResponseEntity.ok(newAddress);
	}
	
	@GetMapping("/get/client/{id}")
	public ResponseEntity<List<IAddressDTO>> getAddressByClientId(@PathVariable Long id){
		List<IAddressDTO> list = repo.getAddressesByClientId(id);
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/get/professional/{id}")
	public ResponseEntity<List<IAddressDTO>> getAddressByProfessionalId(@PathVariable Long id){
		List<IAddressDTO> list = repo.getAddressesByProfessionalId(id);
		return ResponseEntity.ok(list);
	}
	
	@PutMapping("/edit/professional/{id}")
	public ResponseEntity<Address> editProfessionalAddress(@PathVariable Long id, @RequestBody ProfessionalAddressDTO dto){
		Address existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address "+id+" not found", 1003));
		dto.updateEntity(existing);
		Address updated = repo.save(existing);		
		return ResponseEntity.ok(updated);
	}
	
	@PutMapping("/edit/client/{id}")
	public ResponseEntity<Address> editClientAddress(@PathVariable Long id, @RequestBody ClientAddressDTO dto){
		System.out.println(dto.getLongitude());
		System.out.println(dto.getLatitude());
		Address existing = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address "+id+" not found", 1003));
		dto.updateEntity(existing);
		Address updated = repo.save(existing);		
		return ResponseEntity.ok(updated);
	}
	
	@DeleteMapping("/delete/{id}")
	@Transactional
	public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
	    // Verificar existencia
	    Address address = repo.findById(id)
	        .orElseThrow(() -> new ResourceNotFoundException("Address " + id + " not found", 1003));

	    // 🔹 Eliminar vínculos dependientes primero
	    if (professionalRepo.existsByAddress_AddressId(id)) {
	        professionalRepo.deleteByAddress_AddressId(id);
	    }

	    if (clientRepo.existsByAddress_AddressId(id)) {
	        clientRepo.deleteByAddress_AddressId(id);
	    }

	    // 🔹 Luego eliminar la dirección base
	    repo.delete(address);

	    return ResponseEntity.ok("Address and links deleted atomically ✅");
	}

}
