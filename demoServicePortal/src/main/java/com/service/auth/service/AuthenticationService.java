package com.service.auth.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.service.auth.dto.LoginUserDto;
import com.service.auth.dto.RegisterUserDto;
import com.service.auth.enums.Role;
import com.service.auth.model.User;
import com.service.auth.repository.UserRepository;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
        UserRepository userRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUserDto input) {
    	List<Role> roles = (input.getRoles() == null || input.getRoles().isEmpty())
    			? List.of(Role.USER)
    			: input.getRoles().stream().distinct().toList();
    	
        User user = new User()
        		.setRoles(roles)
                .setFullName(input.getFullName())
                .setEmail(input.getEmail())                
                .setPassword(passwordEncoder.encode(input.getPassword()));
        
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
    /*
    public String setRoleToUser(String email, Role newRole) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con email: " + email));

        List<Role> currentRoles = user.getRoles();

        if (!currentRoles.contains(newRole)) {
            currentRoles.add(newRole);
            user.setRoles(currentRoles);
            userRepository.save(user);
            return "Rol asignado";
        }
        else return "El usuario ya tiene asignado el Rol";
    }*/
}