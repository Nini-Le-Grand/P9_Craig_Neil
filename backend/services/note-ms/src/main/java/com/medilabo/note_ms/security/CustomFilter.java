package com.medilabo.note_ms.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * JWT authentication filter that extracts user information from the token
 * and sets it in the Spring Security context.
 */
@Component
public class CustomFilter extends OncePerRequestFilter {

    @Value("${app.secret-key}")
    private String secretKey;

    private static final Logger logger = LogManager.getLogger(CustomFilter.class);

    /**
     * Intercepts requests to extract and validate JWT, then sets the user authentication.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @param filterChain filter chain
     * @throws ServletException in case of servlet errors
     * @throws IOException in case of I/O errors
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("No JWT token found in request for path {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        Claims claims = extractClaims(token);

        if (claims == null) {
            logger.warn("Invalid JWT token for path {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        String userId = claims.getSubject();
        String role = claims.get("role", String.class);

        authenticate(userId, role);
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts claims from a JWT token.
     *
     * @param token JWT token
     * @return Claims object or null if invalid
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
     * Sets authentication in the Spring Security context.
     *
     * @param userId user identifier
     * @param role   user role
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
