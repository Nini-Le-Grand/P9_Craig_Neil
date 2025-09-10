package com.medilabo.user_ms.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.user_ms.domain.dto.LoginDTO;
import com.medilabo.user_ms.domain.dto.PasswordDTO;
import com.medilabo.user_ms.domain.entity.User;
import com.medilabo.user_ms.domain.enums.Role;
import com.medilabo.user_ms.repository.UserRepository;
import com.medilabo.user_ms.utils.JwtUtilsForTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtilsForTest jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = new User();
        user.setId("1");
        user.setEmail("testuser@example.com");
        user.setPassword(passwordEncoder.encode("Password123!"));
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    @Test
    void testLogin_success() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("testuser@example.com");
        loginDTO.setPassword("Password123!");

        mockMvc.perform(post("/auth/login")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(loginDTO)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testLogin_invalidCredentials() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("testuser@example.com");
        loginDTO.setPassword("wrongpassword");

        mockMvc.perform(post("/auth/login")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(loginDTO)))
               .andExpect(status().isUnauthorized())
               .andExpect(jsonPath("$.message").value("Identifiant ou mot de passe incorrecte"));
    }

    @Test
    void testUpdatePassword_success() throws Exception {
        String token = jwtUtils.generateToken("1", "USER", 36000L);

        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setCurrentPassword("Password123!");
        passwordDTO.setNewPassword("newPassword123!");
        passwordDTO.setConfirmPassword("newPassword123!");

        mockMvc.perform(put("/password/update")
                                .header("Authorization", "Bearer " + token)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(passwordDTO)))
               .andExpect(status().isOk())
               .andExpect(content().string("Mot de passe mis à jour avec succès"));
    }

    @Test
    void testUpdatePassword_invalidOldPassword() throws Exception {
        String token = jwtUtils.generateToken("1", "USER", 36000L);

        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setCurrentPassword("wrongPassword123!");
        passwordDTO.setNewPassword("newPassword123");
        passwordDTO.setConfirmPassword("newPassword123");

        mockMvc.perform(put("/password/update")
                                .header("Authorization", "Bearer " + token)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(passwordDTO)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("Veuillez vérifier les données saisies"));
    }

    @Test
    void testUpdatePassword_validationError() throws Exception {
        String token = jwtUtils.generateToken("1", "USER", 36000L);

        PasswordDTO passwordDTO = new PasswordDTO();

        mockMvc.perform(put("/password/update")
                                .header("Authorization", "Bearer " + token)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(passwordDTO)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").exists());
    }
}
