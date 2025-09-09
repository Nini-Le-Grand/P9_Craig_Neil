package com.medilabo.user_ms.security;

import com.medilabo.user_ms.domain.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${app.secret-key}")
    private String secretKey;

    @Value("${app.expiration-time}")
    private Long expirationTime;

    private static final Logger logger = LogManager.getLogger(JwtUtils.class);

    /**
     * Generates the signing key from the configured secret key.
     *
     * @return the SecretKey used for signing JWTs
     */
    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Generates a JWT token for the given user.
     *
     * @param user the authenticated user
     * @return a JWT token as a String
     */
    public String generateToken(User user) {
        logger.info("Generating JWT token for user ID: {}", user.getId());

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());

        String token = Jwts.builder()
                           .setClaims(claims)
                           .setSubject(user.getId().toString())
                           .setIssuedAt(new Date())
                           .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                           .signWith(getSignKey())
                           .compact();

        logger.debug("JWT token generated for user ID: {}", user.getId());
        return token;
    }
}
