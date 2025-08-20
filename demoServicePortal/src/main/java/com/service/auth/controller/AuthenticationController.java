package com.service.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.auth.dto.LoginUserDto;
import com.service.auth.dto.RegisterUserDto;
import com.service.auth.model.User;
import com.service.auth.response.LoginResponse;
import com.service.auth.service.AuthOutcome;
import com.service.auth.service.AuthenticationService;
import com.service.auth.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
    	User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }
    /*
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto dto) {
    	User authenticatedUser = authenticationService.authenticate(dto);
        String jwtToken = jwtService.generateToken(authenticatedUser,dto.getRole());
        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }
    */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDto dto,HttpServletRequest request) {
      String path = request.getHeader("X-Login-Path");   // ej. /professional/login ó /login
      AuthOutcome out = authenticationService.authenticateAndSelectRole(dto, path);

      String token = jwtService.generateToken(out.user().getEmail(), out.roles(),        // todos los roles asignados
          out.activeRole()    // rol activo único
      );

      return ResponseEntity.ok(
          new LoginResponse().setToken(token).setExpiresIn(jwtService.getExpirationTime())
      );
    }

}