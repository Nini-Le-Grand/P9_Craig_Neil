/*
package com.medilabo.users.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.users.dto.LoginDTO;
import com.medilabo.users.domain.enums.Role;
import com.medilabo.users.entity.User;
import com.medilabo.users.repository.UserRepository;
import com.medilabo.users.security.JwtUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RoutingIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtils jwtUtils;
    private String jwtToken;
    private User user;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();

        user = new User();
        user.setEmail("routing@test.com");
        user.setPassword(passwordEncoder.encode("Valid123!"));
        user.setFirstName("Routing");
        user.setLastName("Test");
        user.setRole(Role.PATIENT);

        userRepository.save(user);

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("routing@test.com");
        loginDTO.setPassword("Valid123!");

        String response = mockMvc.perform(post("/auth/login")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(objectMapper.writeValueAsString(loginDTO)))
                                 .andExpect(status().isOk())
                                 .andReturn().getResponse().getContentAsString();

        jwtToken = objectMapper.readTree(response).get("token").asText();
    }

    @Test
    void shouldReturn200_whenValidTokenAndAuthorizedRoute() throws Exception {
        mockMvc.perform(get("/user/profile")
                                .header("Authorization", "Bearer " + jwtToken))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.email").value(user.getEmail())
               );
    }

    @Test
    void shouldReturn401_whenInvalidToken() throws Exception {
        mockMvc.perform(get("/user/profile")
                                .header("Authorization", "Bearer invalid.token.here"))
               .andExpectAll(
                       status().isUnauthorized(),
                       jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()),
                       jsonPath("$.error").value(HttpStatus.UNAUTHORIZED.name()),
                       jsonPath("$.message").value("Authentification requise ou invalide"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    void shouldReturn401_whenMissingToken() throws Exception {
        mockMvc.perform(get("/user/profile"))
               .andExpectAll(
                       status().isUnauthorized(),
                       jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()),
                       jsonPath("$.error").value(HttpStatus.UNAUTHORIZED.name()),
                       jsonPath("$.message").value("Authentification requise ou invalide"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    void shouldReturn401_whenTokenIsExpired() throws Exception {
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        SecretKey key = Keys.hmacShaKeyFor("thisisakeythathasexactly32char!!".getBytes());
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities()
                                      .iterator()
                                      .next()
                                      .getAuthority());
        String expiredToken = Jwts.builder()
                           .claims(claims)
                           .subject(user.getEmail())
                           .issuedAt(new Date(System.currentTimeMillis()))
                           .expiration(new Date(System.currentTimeMillis() + -1))
                           .signWith(key)
                           .compact();

        mockMvc.perform(get("/user/profile")
                                .header("Authorization", "Bearer " + expiredToken))
               .andExpectAll(
                       status().isUnauthorized(),
                       jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()),
                       jsonPath("$.error").value(HttpStatus.UNAUTHORIZED.name()),
                       jsonPath("$.message").value("Authentification requise ou invalide"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    void shouldReturn403_whenForbiddenRoute() throws Exception {
        mockMvc.perform(get("/doctor/**")
               .header("Authorization", "Bearer " + jwtToken))
               .andExpectAll(
                       status().isForbidden(),
                       jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()),
                       jsonPath("$.error").value(HttpStatus.FORBIDDEN.name()),
                       jsonPath("$.message").value("Accès refusé"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    void shouldReturn404_whenUnknownRoute() throws Exception {
        mockMvc.perform(get("/unknown/route")
                                .header("Authorization", "Bearer " + jwtToken))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()),
                       jsonPath("$.error").value(HttpStatus.NOT_FOUND.name()),
                       jsonPath("$.message").value("Route inexistante"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }
}
*/
