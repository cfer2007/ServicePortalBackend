package com.service.auth.service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.service.auth.dto.LoginUserDto;
import com.service.auth.dto.RegisterUserDto;
import com.service.auth.enums.Role;
import com.service.auth.model.User;
import com.service.auth.repository.UserRepository;
import com.service.enums.ProfileStatus;
import com.service.model.Client;
import com.service.model.Professional;
import com.service.repository.ClientRepository;
import com.service.repository.ProfessionalRepository;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;
    
    private final ClientRepository clientRepository;
    
    private final ProfessionalRepository professionalRepository;

    public AuthenticationService(
        UserRepository userRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder,
        ClientRepository clientRepository,
        ProfessionalRepository professionalRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.clientRepository = clientRepository;
        this.professionalRepository = professionalRepository;
    }

    public User signup(RegisterUserDto dto) {
        Role requestedRole = dto.getRole() != null ? dto.getRole() : Role.USER;
        Set<Role> requestedRoles = EnumSet.of(requestedRole);
        User user = userRepository.findByEmail(dto.getEmail()).orElse(null);

        if (user == null) {
            user = new User()
                    .setName(dto.getName())
                    .setEmail(dto.getEmail())
                    .setPassword(passwordEncoder.encode(dto.getPassword()))
                    .setRoles(new ArrayList<>(requestedRoles));

            userRepository.save(user);
        } else {
            boolean updated = user.getRoles().add(requestedRole);
            if (!updated) {          // Ya tenía ese rol, salir rápido
                return user;
            }
            userRepository.save(user);
        }
        ensureProfile(user, requestedRole);

        return user;
    }

    
    private void ensureProfile(User user, Role role) {
        switch (role) {
            case USER -> {
                if (!clientRepository.existsByUserId(user.getId().longValue())) {
                	Client c = new Client();
                	c.setUser(user);
                	c.setName(user.getName());
                    clientRepository.save(c);
                }
            }
            case PROFESSIONAL -> {
                if (!professionalRepository.existsByUserId(user.getId().longValue())) {
                	Professional p = new Professional();
                	p.setName(user.getName());
                	p.setUser(user);
                	p.setStatus(ProfileStatus.PENDING_ID_VERIFICATION);
                    professionalRepository.save(p);
                }
            }
            default -> throw new IllegalArgumentException("Rol no soportado: " + role);
        }
    }


    public User authenticate(LoginUserDto input) {
    	//verificar credenciales
        authenticationManager.authenticate(
        		new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );
        //obtener usuario
        User user = userRepository.findByEmail(input.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User nof found"));
        
        // Validar rol
        if (input.getRole() != null && !user.getRoles().contains(input.getRole())) {
            throw new BadCredentialsException("Role mismatch");
        }
        
        return user;
    }    
}