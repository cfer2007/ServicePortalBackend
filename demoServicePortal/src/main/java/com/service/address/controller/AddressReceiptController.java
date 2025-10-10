package com.service.address.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.address.dto.AddressReceiptDTO;
import com.service.address.model.AddressReceipt;
import com.service.address.repository.AddressReceiptRepository;

@RestController
@RequestMapping("/address_receipt")
public class AddressReceiptController {

	@Autowired
	private AddressReceiptRepository repo;
	
	@PostMapping("/add")
	public ResponseEntity<?> setAddressReceipt(@RequestBody AddressReceiptDTO dto){
		AddressReceipt ar = repo.save(dto.toEntity());
		return ResponseEntity.ok(ar);
	}
}
