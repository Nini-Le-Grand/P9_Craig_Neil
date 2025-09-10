package com.medilabo.user_ms.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.user_ms.domain.dto.UserDTO;
import com.medilabo.user_ms.domain.enums.Role;
import com.medilabo.user_ms.domain.enums.Gender;
import com.medilabo.user_ms.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
class AdminControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private UserDTO sampleUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        sampleUser = UserDTO.builder()
                            .firstName("John")
                            .lastName("Doe")
                            .email("john.doe@example.com")
                            .dateOfBirth(LocalDate.of(1990, 1, 1))
                            .gender(Gender.M)
                            .role(Role.USER)
                            .build();
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldCreateUserSuccessfully() throws Exception {
        mockMvc.perform(post("/admin/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sampleUser)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.firstName").value("John"))
               .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldSearchUsers() throws Exception {
        // Créer un utilisateur
        mockMvc.perform(post("/admin/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sampleUser)))
               .andExpect(status().isOk());

        // Rechercher par prénom
        mockMvc.perform(get("/admin/users/search")
                                .param("keyword", "John"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldGetUserById() throws Exception {
        String json = mockMvc.perform(post("/admin/users")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(sampleUser)))
                             .andReturn().getResponse().getContentAsString();

        UserDTO created = objectMapper.readValue(json, UserDTO.class);

        mockMvc.perform(get("/admin/users/" + created.getId()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldUpdateUser() throws Exception {
        String json = mockMvc.perform(post("/admin/users")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(sampleUser)))
                             .andReturn().getResponse().getContentAsString();

        UserDTO created = objectMapper.readValue(json, UserDTO.class);
        created.setFirstName("Jane");

        mockMvc.perform(put("/admin/users/" + created.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(created)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.firstName").value("Jane"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldResetUserPassword() throws Exception {
        String json = mockMvc.perform(post("/admin/users")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(sampleUser)))
                             .andReturn().getResponse().getContentAsString();

        UserDTO created = objectMapper.readValue(json, UserDTO.class);

        mockMvc.perform(put("/admin/users/password/reset/" + created.getId()))
               .andExpect(status().isOk())
               .andExpect(content().string(org.hamcrest.Matchers.containsString("Mot de passe réinitialisé")));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldDeleteUserSuccessfully() throws Exception {
        String json = mockMvc.perform(post("/admin/users")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(sampleUser)))
                             .andReturn().getResponse().getContentAsString();

        UserDTO created = objectMapper.readValue(json, UserDTO.class);

        mockMvc.perform(delete("/admin/users/" + created.getId()))
               .andExpect(status().isOk())
               .andExpect(content().string("Utilisateur supprimé avec succès"));
    }

    @Test
    @WithMockUser(username = "admin@example.com", roles = {"ADMIN"})
    void shouldReturnValidationErrorOnInvalidEmail() throws Exception {
        sampleUser.setEmail("not-an-email");

        mockMvc.perform(post("/admin/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sampleUser)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.fieldErrors.email").exists());
    }
}
