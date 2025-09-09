package com.medilabo.evaluation_ms.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Custom JWT filter that validates incoming tokens and sets the authentication
 * in the SecurityContext for valid users.
 */
@Component
public class CustomFilter extends OncePerRequestFilter {
    private static final Logger logger = LogManager.getLogger(CustomFilter.class);

    @Value("${app.secret-key}")
    protected String secretKey;

    /**
     * Filters each request to validate JWT token and set authentication.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @param filterChain filter chain
     * @throws ServletException if servlet fails
     * @throws IOException if I/O fails
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        logger.info("Incoming request path: {}", requestPath);

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("No Authorization header found or invalid format for path {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        logger.info("JWT token extracted for path {}: {}", requestPath, token);

        Claims claims = extractClaims(token);
        if (claims == null) {
            logger.warn("JWT validation failed for path {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        String userId = claims.getSubject();
        String role = claims.get("role", String.class);

        logger.info("Token valid: userId={}, role={} for path {}", userId, role, requestPath);

        authenticate(userId, role);

        logger.info("User authenticated: {} with role {} for path {}", userId, role, requestPath);

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts claims from JWT token.
     *
     * @param token JWT token
     * @return Claims if valid, null otherwise
     */
    private Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                       .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                       .build()
                       .parseSignedClaims(token)
                       .getPayload();
        } catch (Exception e) {
            logger.error("Failed to parse JWT token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Sets the authentication in the SecurityContext.
     *
     * @param userId user ID
     * @param role user role
     */
    private void authenticate(String userId, String role) {
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + role)
        );

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userId, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authToken);

        logger.info("SecurityContext updated for user {} with role {}", userId, role);
    }
}
