package com.service.auth.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.service.auth.dto.ChangePasswordRequest;
import com.service.auth.dto.LoginUserDto;
import com.service.auth.dto.RegisterUserDto;
import com.service.auth.enums.Role;
import com.service.auth.model.User;
import com.service.auth.model.UserRole;
import com.service.auth.repository.UserRepository;
import com.service.auth.repository.UserRoleRepository;
import com.service.enums.ProfileStatus;
import com.service.model.Client;
import com.service.model.Professional;
import com.service.repository.ClientRepository;
import com.service.repository.ProfessionalRepository;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ClientRepository clientRepository;
    private final ProfessionalRepository professionalRepository;

    public AuthenticationService(
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            ClientRepository clientRepository,
            ProfessionalRepository professionalRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.clientRepository = clientRepository;
        this.professionalRepository = professionalRepository;
    }

    public User signup(RegisterUserDto dto) {

        Role requestedRole = dto.getRole() != null ? dto.getRole() : Role.USER;

        User user = userRepository.findByEmail(dto.getEmail()).orElse(null);

        if (user == null) {
            user = new User()
                    .setName(dto.getName())
                    .setEmail(dto.getEmail())
                    .setPassword(passwordEncoder.encode(dto.getPassword()));

            userRepository.save(user);
        }

        // ✅ Asegurar que roles no sea null
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }

        boolean hasRole = user.getRoles().stream()
                .anyMatch(r -> r.getRole() == requestedRole);

        if (!hasRole) {
            UserRole ur = new UserRole(user, requestedRole);
            user.getRoles().add(ur);   // agrega al set
            userRoleRepository.save(ur); // guarda en BD
        }

        ensureProfile(user, requestedRole);

        return user;
    }


    // ✅ Crea Client o Professional según rol
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

    // ✅ LOGIN BÁSICO
    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
        );

        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (input.getRole() != null &&
                user.getRoles().stream().noneMatch(r -> r.getRole() == input.getRole())) {
            throw new BadCredentialsException("Role mismatch");
        }

        return user;
    }

    public AuthOutcome authenticateAndSelectRole(LoginUserDto input, String loginPathOrHint) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
        );

        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // ✅ Roles del usuario
        Set<UserRole> roleEntities = user.getRoles();
        Set<Role> userRoles = roleEntities.stream()
                .map(UserRole::getRole)
                .collect(Collectors.toSet());

        String src = (loginPathOrHint == null ? "" : loginPathOrHint).toLowerCase();
        Role hint = input.getRole();
        boolean professionalArea =
                src.contains("/professional/login") || Role.PROFESSIONAL.equals(hint);

        Set<Role> allowed = professionalArea
                ? Set.of(Role.ADMIN, Role.PROFESSIONAL)
                : Set.of(Role.USER);

        Role active = pickActiveRole(userRoles, allowed);
        if (active == null) throw new BadCredentialsException("Role mismatch");

        // ✅ Asegurar que ADMIN use un correo corporativo (tu lógica)
        if (active == Role.ADMIN && !isCorporateEmail(user.getEmail())) {
            throw new BadCredentialsException("Invalid admin email");
        }

        // ✅ Buscar el user_role_id EXACTO
        Long userRoleId = roleEntities.stream()
                .filter(r -> r.getRole() == active)
                .findFirst()
                .map(r -> r.getUserRoleId())        // ID de la fila user_role
                .orElse(null);

        // ✅ DEVOLVER el ID del user_role activo
        return new AuthOutcome(user, userRoles, active, userRoleId);
    }


    private Role pickActiveRole(Set<Role> userRoles, Set<Role> allowed) {
        if (allowed.contains(Role.ADMIN) && userRoles.contains(Role.ADMIN)) return Role.ADMIN;
        if (allowed.contains(Role.PROFESSIONAL) && userRoles.contains(Role.PROFESSIONAL)) return Role.PROFESSIONAL;
        if (allowed.contains(Role.USER) && userRoles.contains(Role.USER)) return Role.USER;
        return null;
    }

    private boolean isCorporateEmail(String email) {
        return email != null && email.toLowerCase().endsWith("@miempresa.com");
    }

    // ✅ OBTENER ROLES DESDE BD
    public Set<Role> getRolesByEmail(String email) {
        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return u.getRoles().stream()
                .map(UserRole::getRole)
                .collect(Collectors.toSet());
    }

    // ✅ ROL POR DEFECTO SEGÚN PRIORIDAD
    public Role pickDefaultRole(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) return Role.USER;
        if (roles.contains(Role.ADMIN)) return Role.ADMIN;
        if (roles.contains(Role.PROFESSIONAL)) return Role.PROFESSIONAL;
        if (roles.contains(Role.USER)) return Role.USER;
        return roles.iterator().next();
    }

    public Role pickDefaultRole(Set<Role> roles, String loginPathOrHint) {
        if (roles == null || roles.isEmpty()) return Role.USER;

        String p = loginPathOrHint == null ? "" : loginPathOrHint.toLowerCase();

        if (p.contains("/admin") && roles.contains(Role.ADMIN)) return Role.ADMIN;
        if (p.contains("/professional") && roles.contains(Role.PROFESSIONAL)) return Role.PROFESSIONAL;
        if ((p.equals("/") || p.contains("/login")) && roles.contains(Role.USER)) return Role.USER;

        return pickDefaultRole(roles);
    }

    // ✅ CAMBIO DE CONTRASEÑA
    public void changePassword(String email, ChangePasswordRequest req) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(req.currentPassword(), user.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        user.setPassword(passwordEncoder.encode(req.newPassword()));
        userRepository.save(user);
    }
    
    public Long getUserRoleId(String email, Role activeRole) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return user.getRoles().stream()
                .filter(r -> r.getRole() == activeRole)
                .findFirst()
                .map(r -> r.getUserRoleId())  // retorna user_role_id
                .orElse(null);
    }

}
