package com.service.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.address.model.AddressReceipt;

public interface AddressReceiptRepository extends JpaRepository<AddressReceipt, Long> {
	

}
