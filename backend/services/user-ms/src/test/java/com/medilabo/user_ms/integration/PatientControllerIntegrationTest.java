package com.medilabo.user_ms.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.user_ms.domain.dto.PatientDTO;
import com.medilabo.user_ms.domain.entity.User;
import com.medilabo.user_ms.domain.enums.Gender;
import com.medilabo.user_ms.domain.enums.Role;
import com.medilabo.user_ms.repository.PatientRepository;
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
class PatientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    private User doctor;
    private User otherDoctor;

    @BeforeEach
    void setUp() {
        patientRepository.deleteAll();
        userRepository.deleteAll();

        doctor = User.builder()
                     .firstName("Doc")
                     .lastName("Test")
                     .email("doc@example.com")
                     .role(Role.USER)
                     .build();
        userRepository.save(doctor);

        otherDoctor = User.builder()
                          .firstName("Other")
                          .lastName("Doctor")
                          .email("other@example.com")
                          .role(Role.USER)
                          .build();
        userRepository.save(otherDoctor);
    }

    private PatientDTO samplePatientDTO() {
        return PatientDTO.builder()
                         .firstName("Alice")
                         .lastName("Dupont")
                         .dateOfBirth(LocalDate.of(1990, 1, 1))
                         .gender(Gender.F)
                         .email("alice@example.com")
                         .doctorId(doctor.getId())
                         .build();
    }

    @Test
    @WithMockUser(username = "doc@example.com", roles = {"USER"})
    void shouldAddAndGetPatient() throws Exception {
        PatientDTO patientDTO = samplePatientDTO();

        String postResponse = mockMvc.perform(post("/patients")
                                                      .contentType(MediaType.APPLICATION_JSON)
                                                      .content(objectMapper.writeValueAsString(patientDTO)))
                                     .andExpect(status().isOk())
                                     .andExpect(jsonPath("$.firstName").value("Alice"))
                                     .andReturn().getResponse().getContentAsString();

        PatientDTO created = objectMapper.readValue(postResponse, PatientDTO.class);

        mockMvc.perform(get("/patients/" + created.getId()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    @WithMockUser(username = "doc@example.com", roles = {"USER"})
    void shouldReturnValidationErrors() throws Exception {
        PatientDTO invalid = PatientDTO.builder()
                                       .firstName("")
                                       .lastName("")
                                       .dateOfBirth(null)
                                       .gender(null)
                                       .email("bad-email")
                                       .doctorId(null)
                                       .build();

        mockMvc.perform(post("/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalid)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.fieldErrors.firstName").value("Le prénom est obligatoire"))
               .andExpect(jsonPath("$.fieldErrors.lastName").value("Le nom est obligatoire"))
               .andExpect(jsonPath("$.fieldErrors.dateOfBirth").value("La date de naissance est obligatoire"))
               .andExpect(jsonPath("$.fieldErrors.gender").value("Le genre est obligatoire"))
               .andExpect(jsonPath("$.fieldErrors.email").value("must be a well-formed email address"))
               .andExpect(jsonPath("$.fieldErrors.doctorId").value("Le médecin est obligatoire"));
    }

    @Test
    @WithMockUser(username = "doc@example.com", roles = {"USER"})
    void shouldNotAddPatientWithDuplicateEmail() throws Exception {
        PatientDTO patient1 = samplePatientDTO();
        PatientDTO patient2 = samplePatientDTO();

        mockMvc.perform(post("/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patient1)))
               .andExpect(status().isOk());

        mockMvc.perform(post("/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patient2)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.fieldErrors.email").value("Cet email est déjà utilisé"));
    }

    @Test
    @WithMockUser(username = "doc@example.com", roles = {"USER"})
    void shouldUpdatePatient() throws Exception {
        PatientDTO created = samplePatientDTO();
        String json = mockMvc.perform(post("/patients")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(created)))
                             .andReturn().getResponse().getContentAsString();
        PatientDTO saved = objectMapper.readValue(json, PatientDTO.class);

        saved.setFirstName("Updated");
        mockMvc.perform(put("/patients/" + saved.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(saved)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.firstName").value("Updated"));
    }

    @Test
    @WithMockUser(username = "doc@example.com", roles = {"USER"})
    void shouldDeletePatient() throws Exception {
        PatientDTO created = samplePatientDTO();
        String json = mockMvc.perform(post("/patients")
                                              .contentType(MediaType.APPLICATION_JSON)
                                              .content(objectMapper.writeValueAsString(created)))
                             .andReturn().getResponse().getContentAsString();
        PatientDTO saved = objectMapper.readValue(json, PatientDTO.class);

        mockMvc.perform(delete("/patients/" + saved.getId()))
               .andExpect(status().isOk())
               .andExpect(content().string("Le patient a été supprimé avec succès"));
    }

    @Test
    @WithMockUser(username = "doc@example.com", roles = {"USER"})
    void shouldReturnNotFoundForNonExistingPatient() throws Exception {
        mockMvc.perform(get("/patients/non-existing-id"))
               .andExpect(status().isNotFound());
    }
}
