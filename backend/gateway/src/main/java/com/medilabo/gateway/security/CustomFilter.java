package com.medilabo.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Custom JWT filter for the Gateway.
 * <p>
 * Responsibilities:
 * - Intercepts API requests.
 * - Skips authentication for public routes (/frontend, devtools, login).
 * - Validates JWT and sets a reactive SecurityContext.
 * - Logs all key steps for debugging.
 */
@Component
public class CustomFilter implements WebFilter {

    @Value("${app.secret-key}")
    private String secretKey;

    private static final Logger logger = LogManager.getLogger(CustomFilter.class);

    /**
     * Intercepts each request, verifies JWT if present, and sets SecurityContext.
     *
     * @param exchange current server web exchange
     * @param chain filter chain
     * @return Mono that completes when processing is done
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        logger.info("Incoming request path: {}", path);

        // Public routes
        if (path.contains("/frontend") || path.contains("devtools")) {
            logger.info("Public path, skipping JWT authentication");
            return chain.filter(exchange);
        }

        if (path.equals("/api/users/auth/login")) {
            logger.info("Login path, skipping JWT authentication");
            return chain.filter(exchange);
        }

        // Extract JWT from Authorization header
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("Missing or invalid Authorization header for path: {}", path);
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);
        logger.info("JWT token received: {}", token);

        Claims claims = extractClaims(token);
        if (claims == null) {
            logger.warn("Invalid JWT token for path: {}", path);
            return chain.filter(exchange);
        }
        logger.info("JWT claims extracted: subject={}, role={}", claims.getSubject(), claims.get("role", String.class));

        SecurityContextImpl securityContext = authenticate(claims);
        logger.info("SecurityContext set for user: {}", claims.getSubject());

        return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
    }

    /**
     * Parses the JWT and extracts claims.
     *
     * @param token signed JWT token
     * @return Claims or null if invalid
     */
    private Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                       .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                       .build()
                       .parseSignedClaims(token)
                       .getPayload();
        } catch (Exception e) {
            logger.error("Failed to parse JWT token", e);
            return null;
        }
    }

    /**
     * Creates a SecurityContext from JWT claims.
     *
     * @param claims JWT claims
     * @return SecurityContextImpl ready for Spring Security
     */
    private SecurityContextImpl authenticate(Claims claims) {
        String role = claims.get("role", String.class);
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);

        return new SecurityContextImpl(authToken);
    }
}
