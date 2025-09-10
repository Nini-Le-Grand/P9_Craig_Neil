package com.medilabo.user_ms.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtilsForTest {
    private static final String SECRET_KEY = "thisisakeythathasexactly32char!!";
    public String generateToken(String subject, String role, long expirationTime) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                   .claims(claims)
                   .subject(subject)
                   .issuedAt(new Date())
                   .expiration(new Date(System.currentTimeMillis() + expirationTime))
                   .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                   .compact();
    }
}
