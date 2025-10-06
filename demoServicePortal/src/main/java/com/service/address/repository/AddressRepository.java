package com.service.address.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.service.address.dto.IAddressDTO;
import com.service.address.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{

	@Query(value = "select a.name, a.address_id as addressId, r.region_id as regionId, r.name as regionName, \n"
			+ "a.city_id as cityId, c.name as cityName, a.street_address as streetAddress, a.latitude, a.longitude \n"
			+ "from address a, city c, region r \n"
			+ "where client_id = ?1\n"
			+ "and a.city_id=c.city_id \n"
			+ "and c.region_id=r.region_id", nativeQuery = true)
	List<IAddressDTO> getAdressesByClientId(Long id);
	
	@Query(value = "select a.name, a.address_id as addressId, r.region_id as regionId, r.name as regionName, a.city_id as cityId, c.name as cityName, a.street_address as streetAddress\n"
			+ "from address a, city c, region r\n"
			+ "where professional_id = ?1\n"
			+ "and a.city_id=c.city_id\n"
			+ "and c.region_id=r.region_id", nativeQuery = true)
	List<IAddressDTO> getAddressesByProfessionalId(Long id);
}
