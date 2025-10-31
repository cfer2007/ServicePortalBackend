package com.service.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.auth.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long>{

}
