package com.service.address.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.service.address.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{

	@Query(value = "select * from address where client_id =?1", nativeQuery = true)
	List<Address> getAdressesByClientId(Long id);
	
	@Query(value = "select * from address where professional_id =?1", nativeQuery = true)
	List<Address> getAddressesByProfessionalId(Long id);
}
