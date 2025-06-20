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

    public User signup(RegisterUserDto input) {
        List<Role> requestedRoles = (input.getRoles() == null || input.getRoles().isEmpty())
                ? List.of(Role.USER)
                : input.getRoles().stream().distinct().toList();

        User user = userRepository.findByEmail(input.getEmail()).orElse(null);

        if (user == null) {
            // Crear nuevo usuario
            user = new User()
                    .setRoles(requestedRoles)
                    .setFullName(input.getFullName())
                    .setEmail(input.getEmail())
                    .setPassword(passwordEncoder.encode(input.getPassword()));
            user = userRepository.save(user);
        } else {
            // Usuario ya existe, agregar nuevos roles si no están presentes
            List<Role> existingRoles = user.getRoles();
            boolean updated = false;

            for (Role role : requestedRoles) {
                if (!existingRoles.contains(role)) {
                    existingRoles.add(role);
                    updated = true;
                }
            }

            if (updated) {
                user.setRoles(existingRoles);
                user = userRepository.save(user);
            }
        }

        // Asociar perfil según roles (si aún no existe)
        for (Role role : requestedRoles) {
            switch (role) {
                case USER -> {
                    if (!clientRepository.existsByUserId(user.getId().longValue())) {
                        Client cliente = new Client();
                        cliente.setUser(user);
                        clientRepository.save(cliente);
                    }
                }
                case PROFESSIONAL -> {
                    if (!professionalRepository.existsByUserId(user.getId().longValue())) {
                        Professional professional = new Professional();
                        professional.setUser(user);
                        professionalRepository.save(professional);
                    }
                }
			default -> throw new IllegalArgumentException("Unexpected value: " + role);
            }
        }

        return user;
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
}