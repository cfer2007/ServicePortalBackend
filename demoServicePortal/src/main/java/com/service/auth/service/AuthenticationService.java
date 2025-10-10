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
                	p.setStatus(ProfileStatus.PENDING_PROFILE_INFO);
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
    
    public AuthOutcome authenticateAndSelectRole(LoginUserDto input, String loginPathOrHint) {
        // 1) Autentica credenciales
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
        );

        // 2) Carga usuario y roles reales
        User user = userRepository.findByEmail(input.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<Role> userRoles = new java.util.HashSet<>(user.getRoles()); // Set<Role>

        // 3) Determina área a partir de la ruta o del hint (no confiable)
        String src = (loginPathOrHint == null ? "" : loginPathOrHint).toLowerCase();
        Role hint = input.getRole(); // opcional; úsalo solo como pista
        boolean professionalArea =
            src.contains("/professional/login") || Role.PROFESSIONAL.equals(hint);

        // 4) Roles permitidos por área
        java.util.Set<Role> allowed = professionalArea
            ? java.util.Set.of(Role.ADMIN, Role.PROFESSIONAL)
            : java.util.Set.of(Role.USER);

        // 5) Elegir rol ACTIVO con prioridad ADMIN > PROFESSIONAL > USER
        Role active = pickActiveRole(userRoles, allowed);
        if (active == null) throw new BadCredentialsException("Role mismatch for this area");

        // 6) Reglas extra (opcional): admin corporativo
        if (active == Role.ADMIN && !isCorporateEmail(user.getEmail())) {
          throw new BadCredentialsException("Invalid admin email");
        }

        return new AuthOutcome(user, userRoles, active);
      }
    
    private Role pickActiveRole(Set<Role> userRoles, Set<Role> allowed) {
        if (allowed.contains(Role.ADMIN) && userRoles.contains(Role.ADMIN)) return Role.ADMIN;
        if (allowed.contains(Role.PROFESSIONAL) && userRoles.contains(Role.PROFESSIONAL)) return Role.PROFESSIONAL;
        if (allowed.contains(Role.USER) && userRoles.contains(Role.USER)) return Role.USER;
        return null;
      }

      private boolean isCorporateEmail(String email) {
        return email != null && email.toLowerCase().endsWith("@miempresa.com"); // ajusta dominio
      }
      
   // --- Helpers de roles (wrappers simples) ---

      /** Convierte los roles del usuario a Set<Role> sin duplicados, seguro para listas vacías. */
      private Set<Role> toRoleSet(User u) {
        if (u == null || u.getRoles() == null || u.getRoles().isEmpty()) {
          return EnumSet.noneOf(Role.class);
        }
        // EnumSet requiere no vacío; usa HashSet primero y crea EnumSet si hay elementos
        java.util.Set<Role> tmp = new java.util.HashSet<>(u.getRoles());
        return tmp.isEmpty() ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(tmp);
      }

      /** Carga roles por email como Set<Role>. */
      public Set<Role> getRolesByEmail(String email) {
        User u = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        return toRoleSet(u);
      }

      /** Rol por defecto con prioridad ADMIN > PROFESSIONAL > USER. */
      public Role pickDefaultRole(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) return Role.USER; // o lanza excepción si lo prefieres
        if (roles.contains(Role.ADMIN)) return Role.ADMIN;
        if (roles.contains(Role.PROFESSIONAL)) return Role.PROFESSIONAL;
        if (roles.contains(Role.USER)) return Role.USER;
        // Fallback defensivo
        return roles.iterator().next();
      }

      /** Variante que intenta respetar el path/hint (e.g. "/professional/login"), con fallback a prioridad fija. */
      public Role pickDefaultRole(Set<Role> roles, String loginPathOrHint) {
        if (roles == null || roles.isEmpty()) return Role.USER;
        String p = loginPathOrHint == null ? "" : loginPathOrHint.toLowerCase();
        if (p.contains("/admin") && roles.contains(Role.ADMIN)) return Role.ADMIN;
        if (p.contains("/professional") && roles.contains(Role.PROFESSIONAL)) return Role.PROFESSIONAL;
        if ((p.equals("/") || p.contains("/login")) && roles.contains(Role.USER)) return Role.USER;
        return pickDefaultRole(roles);
      }

}