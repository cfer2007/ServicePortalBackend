package com.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.service.model.Profession;

public interface ProfessionRepository extends JpaRepository<Profession, Long>{
	
}
