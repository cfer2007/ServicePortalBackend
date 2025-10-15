package com.service.address.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.service.address.dto.IAddressDTO;
import com.service.address.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{

	@Query(value = """
		    SELECT 
		        a.name,
		        a.address_id AS addressId,
		        r.region_id AS regionId,
		        r.name AS regionName,
		        a.city_id AS cityId,
		        c.name AS cityName,
		        a.street_address AS streetAddress,
		        a.latitude,
		        a.longitude
		    FROM address a
		    JOIN address_client ac ON a.address_id = ac.address_id
		    JOIN city c ON a.city_id = c.city_id
		    JOIN region r ON c.region_id = r.region_id
		    WHERE ac.client_id = ?1
		""", nativeQuery = true)
		List<IAddressDTO> getAddressesByClientId(Long id);
	
	@Query(value = """
		    SELECT 
		        a.name,
		        a.address_id AS addressId,
		        r.region_id AS regionId,
		        r.name AS regionName,
		        a.city_id AS cityId,
		        c.name AS cityName,
		        a.street_address AS streetAddress,
		        a.latitude,
		        a.longitude
		    FROM address a
		    JOIN address_professional ap ON a.address_id = ap.address_id
		    JOIN city c ON a.city_id = c.city_id
		    JOIN region r ON c.region_id = r.region_id
		    WHERE ap.professional_id = ?1
		""", nativeQuery = true)
		List<IAddressDTO> getAddressesByProfessionalId(Long id);
}
