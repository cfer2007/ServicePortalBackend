package com.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.service.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long>{
	boolean existsByUserId(Long id);
	
	@Query(value = "select * from client where user_id = (select id from users where email = ?1)", nativeQuery = true)
	Client findByEmail(@Param("id")String email);
}
