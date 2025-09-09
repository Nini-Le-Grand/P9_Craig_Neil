/*
package com.medilabo.users.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtils = new JwtUtils();

        Field secretKeyField = JwtUtils.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);

        String secretKey = "12345678901234567890123456789012"; // 32 chars for HS256 key
        secretKeyField.set(jwtUtils, secretKey);

        Field expirationField = JwtUtils.class.getDeclaredField("expirationTime");
        expirationField.setAccessible(true);

        long expirationTime = 1000L * 60 * 60; // 1 hour
        expirationField.set(jwtUtils, expirationTime);
    }

    private UserDetails createUserDetails(String username) {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.singletonList(authority);
            }
            @Override public String getPassword() { return "password"; }
            @Override public String getUsername() { return username; }
            @Override public boolean isAccountNonExpired() { return true; }
            @Override public boolean isAccountNonLocked() { return true; }
            @Override public boolean isCredentialsNonExpired() { return true; }
            @Override public boolean isEnabled() { return true; }
        };
    }

    @Test
    void generateToken_shouldCreateTokenContainingUsernameAndRole() {
        UserDetails userDetails = createUserDetails("test@example.com");
        String token = jwtUtils.generateToken(userDetails);
        assertNotNull(token);

        Claims claims = jwtUtils.extractClaims(token);
        assertEquals("test@example.com", claims.getSubject());
        assertEquals("ROLE_USER", claims.get("role"));
    }

    @Test
    void isTokenValid_shouldReturnTrueForValidToken() {
        UserDetails userDetails = createUserDetails("valid@example.com");
        String token = jwtUtils.generateToken(userDetails);
        assertTrue(jwtUtils.isTokenValid(token, userDetails));
    }

    @Test
    void isTokenValid_shouldThrowJwtExceptionForInvalidUser() {
        UserDetails userDetails = createUserDetails("valid@example.com");
        String token = jwtUtils.generateToken(userDetails);

        UserDetails differentUser = createUserDetails("other@example.com");
        JwtException ex = assertThrows(JwtException.class, () -> jwtUtils.isTokenValid(token, differentUser));
        System.out.println(ex.getMessage());
        assertTrue(ex.getMessage().contains("Invalid Token"));
    }

    @Test
    void isTokenValid_shouldThrowJwtExceptionForExpiredToken() throws Exception {
        Field expirationField = JwtUtils.class.getDeclaredField("expirationTime");
        expirationField.setAccessible(true);
        expirationField.set(jwtUtils, -1000L);  // token expired 1 second ago

        UserDetails userDetails = createUserDetails("expired@example.com");
        String token = jwtUtils.generateToken(userDetails);

        JwtException ex = assertThrows(JwtException.class, () -> jwtUtils.isTokenValid(token, userDetails));
        System.out.println(ex.getMessage());
        assertTrue(ex.getMessage().contains("Invalid Token"));
    }

    @Test
    void extractClaims_shouldThrowExceptionForInvalidToken() {
        String invalidToken = "this.is.an.invalid.token";
        JwtException ex = assertThrows(JwtException.class, () -> jwtUtils.extractClaims(invalidToken));
    }
}
*/
