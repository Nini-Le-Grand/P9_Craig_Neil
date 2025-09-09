/*
package com.medilabo.users.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.users.dto.LoginDTO;
import com.medilabo.users.domain.enums.Role;
import com.medilabo.users.entity.User;
import com.medilabo.users.repository.UserRepository;
import com.medilabo.users.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtUtils jwtUtils;
    private final String email = "user@example.com";
    private final String rawPassword = "Password123!";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(Role.PATIENT);

        userRepository.save(user);
    }

    @Test
    void login_shouldReturnToken_whenValidCredentials() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(email);
        loginDTO.setPassword(rawPassword);

        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                                           .content(objectMapper.writeValueAsString(loginDTO)))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.token").isString(),
                       jsonPath("$.token", not(emptyOrNullString()))
               );
    }

    @Test
    void login_shouldReturn400_whenMissingFields() throws Exception {
        LoginDTO loginDTO = new LoginDTO();

        mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginDTO)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.status").value(400),
                       jsonPath("$.error").value("BAD_REQUEST"),
                       jsonPath("$.message").value("Invalid Format"),
                       jsonPath("$.fieldErrors.email").value("L'email est obligatoire"),
                       jsonPath("$.fieldErrors.password").value("Le mot de passe est obligatoire")
               );
    }

    @Test
    void login_shouldReturn401_whenUserDoesNotExist() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("unknown@example.com");
        loginDTO.setPassword("anyPassword");

        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                                           .content(objectMapper.writeValueAsString(loginDTO)))
               .andExpectAll(
                       status().isUnauthorized(),
                       jsonPath("$.status").value(401),
                       jsonPath("$.error").value("UNAUTHORIZED"),
                       jsonPath("$.message").value("Identifiant ou mot de passe incorrecte"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    void login_shouldReturn401_whenPasswordIsIncorrect() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(email);
        loginDTO.setPassword("WrongPassword!");

        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                                           .content(objectMapper.writeValueAsString(loginDTO)))
               .andExpectAll(
                       status().isUnauthorized(),
                       jsonPath("$.status").value(401),
                       jsonPath("$.error").value("UNAUTHORIZED"),
                       jsonPath("$.message").value("Identifiant ou mot de passe incorrecte"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    void login_shouldReturnToken_whenTokenIsAlreadyPresent() throws Exception {
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(email)
                                                                                    .password(rawPassword)
                                                                                    .roles("PATIENT")
                                                                                    .build();

        String existingToken = jwtUtils.generateToken(userDetails);

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(email);
        loginDTO.setPassword(rawPassword);

        mockMvc.perform(post("/auth/login").header("Authorization", "Bearer " + existingToken)
                                           .contentType(MediaType.APPLICATION_JSON)
                                           .content(objectMapper.writeValueAsString(loginDTO)))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.token").isString(),
                       jsonPath("$.token", not(emptyOrNullString()))
               );
    }

    @Test
    void login_shouldReturnToken_whenInvalidToken() throws Exception {
        String existingToken = "invalid token";

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(email);
        loginDTO.setPassword(rawPassword);

        mockMvc.perform(post("/auth/login").header("Authorization", "Bearer " + existingToken)
                                           .contentType(MediaType.APPLICATION_JSON)
                                           .content(objectMapper.writeValueAsString(loginDTO)))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.token").isString(),
                       jsonPath("$.token", not(emptyOrNullString()))
               );
    }
}
*/
