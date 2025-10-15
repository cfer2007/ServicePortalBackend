package com.service.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.service.address.model.AddressProfessional;
import java.util.List;

public interface AddressProfessionalRepository extends JpaRepository<AddressProfessional, Long> {
    List<AddressProfessional> findByProfessional_ProfessionalId(Long professionalId);
    boolean existsByAddress_AddressIdAndProfessional_ProfessionalId(Long addressId, Long professionalId);
    void deleteByAddress_AddressId(Long addressId);
    boolean existsByAddress_AddressId(Long addressId);
}