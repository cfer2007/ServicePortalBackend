package com.service.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.service.auth.enums.Role;

@Service
public class JwtService {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    @Value("${security.jwt.refresh-expiration-time}") // nueva: REFRESH
    private long refreshExpiration;
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails, Role loginRole) {
    	Map<String, Object> claims = new HashMap<>();
        claims.put("role", loginRole);
        return generateToken(claims, userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }

    @SuppressWarnings("deprecation")
	private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
    	//extraClaims.put("role", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
    	
    	return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, getSignInKey())
                .compact();
    }  

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @SuppressWarnings("deprecation")
    public Claims extractAllClaims(String token) {
      return Jwts.parser()
        .setSigningKey(getSignInKey())
        .setAllowedClockSkewSeconds(5) // pequeña tolerancia anti “race”
        .parseClaimsJws(token)
        .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    /*
    public String generateToken(String subject, Set<Role> allRoles, Role activeRole) {
    	  Map<String, Object> claims = new java.util.HashMap<>();
    	  claims.put("roles", allRoles.stream().map(Role::name).toArray(String[]::new));
    	  claims.put("role", activeRole.name());
    	  return buildToken(claims, subject, jwtExpiration);
    }*/

    @SuppressWarnings("deprecation")
	private String buildToken(Map<String, Object> extraClaims, String subject, long expiration) {
    	return Jwts.builder()
    	      .setClaims(extraClaims)
    	      .setSubject(subject)
    	      .setIssuedAt(new Date())
    	      .setExpiration(new Date(System.currentTimeMillis() + expiration))
    	      .signWith(SignatureAlgorithm.HS256, getSignInKey())
    	      .compact();
    }
    public String generateAccessToken(String subject, String name, Set<Role> roles, Role activeRole, Long userRoleId) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("typ", "access");
        claims.put("name", name);
        if (roles != null) claims.put("roles", roles.stream().map(Role::name).toArray(String[]::new));
        if (activeRole != null) claims.put("role", activeRole.name());
        if (userRoleId != null) claims.put("userRoleId", userRoleId);
        return buildToken(claims, subject, jwtExpiration);
    }


    public String generateRefreshToken(String subject) {
    	System.out.println("generateRefreshToken");
    	Map<String, Object> claims = new HashMap<>();
        claims.put("typ", "refresh");
        return buildToken(claims, subject, refreshExpiration);
    }

    public boolean isRefreshToken(String token) {
        //String typ = extractClaim(token, c -> (String) c.get("typ"));
        //return "refresh".equalsIgnoreCase(typ);
    	return "refresh".equalsIgnoreCase(extractClaim(token, c -> (String)c.get("typ")));
    }

}