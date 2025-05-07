package com.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.model.Professional;

public interface ProfessionalRepository extends JpaRepository<Professional, Long> {

}
