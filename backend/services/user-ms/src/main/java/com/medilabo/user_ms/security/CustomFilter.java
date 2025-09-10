package com.medilabo.user_ms.security;

import com.medilabo.user_ms.domain.entity.User;
import com.medilabo.user_ms.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;

@Component
public class CustomFilter extends OncePerRequestFilter {

    private static final Logger logger = LogManager.getLogger(CustomFilter.class);

    @Autowired
    private UserRepository userRepository;

    @Value("${app.secret-key}")
    protected String secretKey;

    /**
     * Filters incoming requests to authenticate users based on JWT token.
     *
     * @param request     the HTTP request
     * @param response    the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException in case of servlet errors
     * @throws IOException      in case of IO errors
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("No Bearer token found for request path: {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        logger.debug("JWT token found for request path: {}", requestPath);

        Claims claims = extractClaims(token);
        if (claims == null) {
            logger.warn("Invalid JWT token for request path: {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        String userId = claims.getSubject();
        String role = claims.get("role", String.class);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            logger.warn("User not found with ID from JWT: {}", userId);
            filterChain.doFilter(request, response);
            return;
        }

        User user = userOpt.get();
        authenticate(user, role);
        logger.info("Authenticated user ID: {} with role: {}", userId, role);

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts claims from a JWT token.
     *
     * @param token the JWT token
     * @return Claims object if valid, null otherwise
     */
    private Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                       .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                       .build()
                       .parseSignedClaims(token)
                       .getPayload();
        } catch (Exception e) {
            logger.error("Failed to extract claims from JWT: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Authenticates the user and sets the SecurityContext.
     *
     * @param user the authenticated user
     * @param role the user's role
     */
    private void authenticate(User user, String role) {
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + role)
        );

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authToken);
        logger.debug("User ID {} set in SecurityContext with authorities: {}", user.getId(), authorities);
    }
}
