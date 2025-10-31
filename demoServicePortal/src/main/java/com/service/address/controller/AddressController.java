package com.service.address.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.service.address.dto.AddressDTO;
import com.service.address.dto.IAddressDTO;
import com.service.address.model.Address;
import com.service.address.repository.AddressRepository;
import com.service.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressRepository repo;

    // ✅ Crear o agregar una dirección (cliente o profesional)
    @PostMapping("/add")
    @Transactional
    public ResponseEntity<Address> addAddress(@RequestBody AddressDTO dto) {
    	System.out.println(dto.getStreetAddress());
    	System.out.println(dto.getCityId());
    	System.out.println(dto.getLatitude());
    	System.out.println(dto.getLongitude());
    	System.out.println(dto.getUserRoleId());
    	System.out.println(dto.getDocumentId());
    	
        Address newAddress = repo.save(dto.toEntity());
        return ResponseEntity.ok(newAddress);
    }

    // ✅ Obtener direcciones por user_role_id (cliente o profesional)
    @GetMapping("/get/byUserRole/{userRoleId}")
    public ResponseEntity<List<IAddressDTO>> getAddressesByUserRole(@PathVariable Long userRoleId) {
        List<IAddressDTO> list = repo.getAddressesByUserRoleId(userRoleId);
        return ResponseEntity.ok(list);
    }

    // ✅ Editar una dirección
    @PutMapping("/edit/{id}")
    public ResponseEntity<Address> editAddress(@PathVariable Long id, @RequestBody AddressDTO dto) {
        Address existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address " + id + " not found", 1003));

        dto.updateEntity(existing);
        Address updated = repo.save(existing);
        return ResponseEntity.ok(updated);
    }

    // ✅ Eliminar una dirección
    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        Address address = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address " + id + " not found", 1003));

        repo.delete(address);
        return ResponseEntity.ok("Address deleted successfully ✅");
    }
}
