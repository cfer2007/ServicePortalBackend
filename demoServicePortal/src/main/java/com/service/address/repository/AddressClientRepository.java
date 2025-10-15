package com.service.address.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.address.model.AddressClient;

public interface AddressClientRepository extends JpaRepository<AddressClient, Long> {
    List<AddressClient> findByClient_ClientId(Long clientId);
    boolean existsByAddress_AddressIdAndClient_ClientId(Long addressId, Long clientId);
    void deleteByAddress_AddressId(Long addressId);
    boolean existsByAddress_AddressId(Long addressId);
}