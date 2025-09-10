package com.medilabo.user_ms.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.user_ms.domain.dto.UserDTO;
import com.medilabo.user_ms.domain.entity.User;
import com.medilabo.user_ms.domain.enums.Gender;
import com.medilabo.user_ms.domain.enums.Role;
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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        User user = new User();
        user.setEmail("alice@example.com");
        user.setPassword("encoded-password");
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    @Test
    @WithMockUser(username = "alice@example.com", roles = {"USER"})
    void shouldGetConnectedUserProfile() throws Exception {
        mockMvc.perform(get("/user/profile"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.email", is("alice@example.com")));
    }

    @Test
    @WithMockUser(username = "alice@example.com", roles = {"USER"})
    void shouldUpdateUserProfile() throws Exception {
        UserDTO update = UserDTO.builder()
                                .firstName("Alice")
                                .lastName("Dupont")
                                .dateOfBirth(LocalDate.of(1990, 1, 1))
                                .gender(Gender.F)
                                .email("alice_new@example.com")
                                .role(Role.USER)
                                .build();

        mockMvc.perform(put("/user/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(update)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.email", is("alice_new@example.com")))
               .andExpect(jsonPath("$.firstName", is("Alice")))
               .andExpect(jsonPath("$.lastName", is("Dupont")));
    }

    @Test
    @WithMockUser(username = "alice@example.com", roles = {"USER"})
    void shouldReturnBadRequestWhenUpdatingWithInvalidData() throws Exception {
        UserDTO invalidUpdate = UserDTO.builder()
                                       .firstName("")
                                       .lastName("Dupont")
                                       .dateOfBirth(LocalDate.of(1990, 1, 1))
                                       .gender(Gender.F)
                                       .email("bad-email")
                                       .build();

        mockMvc.perform(put("/user/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidUpdate)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.fieldErrors.firstName").value("Le prénom est obligatoire"))
               .andExpect(jsonPath("$.fieldErrors.email").value("must be a well-formed email address"));
    }


}