package com.service.auth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.service.auth.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
        JwtService jwtService,
        UserDetailsService userDetailsService
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 0) Rutas públicas o preflight
    	final String uri = request.getRequestURI();
    	if ("OPTIONS".equalsIgnoreCase(request.getMethod())
    	    || uri.startsWith("/auth/login")
    	    || uri.startsWith("/auth/signup")
    	    || uri.startsWith("/auth/refresh")) {
    	  filterChain.doFilter(request, response);
    	  return;
    	}

        // 1) Sin Authorization -> deja pasar (la config de seguridad decidirá)
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        try {
            // 2) Extrae usuario (si expira lanza ExpiredJwtException)
            final String userEmail = jwtService.extractUsername(jwt);

            Authentication current = SecurityContextHolder.getContext().getAuthentication();
            if (userEmail != null && current == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            // 3) Continúa la cadena
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            send401(response, "TOKEN_EXPIRED");
        } catch (JwtException | IllegalArgumentException ex) {
            send401(response, "TOKEN_INVALID");
        }
    }

    private void send401(HttpServletResponse response, String code) throws IOException {
        if (response.isCommitted()) return;
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" + code + "\"}");
        response.getWriter().flush();
    }
}
