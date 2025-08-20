package com.service.auth.service;

import java.util.Set;

import com.service.auth.enums.Role;
import com.service.auth.model.User;

//paquete com.service.auth.service
public record AuthOutcome(User user, Set<Role> roles, Role activeRole) {}

