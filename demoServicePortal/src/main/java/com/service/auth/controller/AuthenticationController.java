package com.service.auth.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.auth.dto.LoginUserDto;
import com.service.auth.dto.RefreshRequest;
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
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginUserDto dto,HttpServletRequest request) {
      String path = request.getHeader("X-Login-Path");   // ej. /professional/login ó /login
      AuthOutcome out = authenticationService.authenticateAndSelectRole(dto, path);

      String token = jwtService.generateToken(out.user().getEmail(), out.roles(),        // todos los roles asignados
          out.activeRole()    // rol activo único
      );
      
      String refresh = jwtService.generateRefreshToken(out.user().getEmail());


      return ResponseEntity.ok(
          new LoginResponse()
          .setToken(token)
          .setRefreshToken(refresh)
          .setExpiresIn(jwtService.getExpirationTime())
          
      );
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest req) {
    	  if (req == null || req.refreshToken() == null) {
    	    return ResponseEntity.badRequest().body(Map.of("error","MISSING_RT"));
    	  }
    	  String rt = req.refreshToken();

    	  try {
    	    // Primero verifica firma/exp
    	    if (jwtService.isTokenExpired(rt)) {
    	      return ResponseEntity.status(401).body(Map.of("error","RT_EXPIRED"));
    	    }
    	    if (!jwtService.isRefreshToken(rt)) {
    	      return ResponseEntity.status(401).body(Map.of("error","NOT_REFRESH"));
    	    }
    	  } catch (io.jsonwebtoken.ExpiredJwtException e) {
    	    return ResponseEntity.status(401).body(Map.of("error","RT_EXPIRED_EX"));
    	  } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
    	    return ResponseEntity.status(401).body(Map.of("error","RT_INVALID_SIGNATURE"));
    	  }

    	  String email = jwtService.extractUsername(rt);
    	  var roles = authenticationService.getRolesByEmail(email);
    	  var active = authenticationService.pickDefaultRole(roles);
    	  String newAccess = jwtService.generateAccessToken(email, roles, active);

    	  return ResponseEntity.ok(
    	      new LoginResponse().setToken(newAccess)
    	                         .setExpiresIn(jwtService.getExpirationTime())
    	                         .setRefreshToken(null) // usamos el mismo RT hasta que caduque
    	  );
    	}

}