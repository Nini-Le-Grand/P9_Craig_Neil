/*
package com.medilabo.users.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.users.dto.PasswordDTO;
import com.medilabo.users.dto.PatientDTO;
import com.medilabo.users.domain.enums.Gender;
import com.medilabo.users.domain.enums.Role;
import com.medilabo.users.entity.User;
import com.medilabo.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ObjectMapper objectMapper;
    private User patient;
    private User doctor;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        doctor = new User();
        doctor.setEmail("doctor@example.com");
        doctor.setFirstName("Test");
        doctor.setLastName("Doctor");
        doctor.setDateOfBirth(LocalDate.of(1990, 1, 1));
        doctor.setGender(Gender.M);
        doctor.setRole(Role.DOCTOR);
        doctor.setPassword(passwordEncoder.encode("OldPass123!"));
        userRepository.save(doctor);

        patient = new User();
        patient.setEmail("patient@example.com");
        patient.setFirstName("Test");
        patient.setLastName("Patient");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setGender(Gender.M);
        patient.setRole(Role.PATIENT);
        patient.setPassword(passwordEncoder.encode("OldPass123!"));
        patient.setDoctor(doctor);
        userRepository.save(patient);
    }

    @Test
    void getUserProfile_shouldReturnUserProfile() throws Exception {
        mockMvc.perform(get("/user/profile").with(user(patient.getEmail())))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.email").value(patient.getEmail()),
                       jsonPath("$.firstName").value(patient.getFirstName()),
                       jsonPath("$.lastName").value(patient.getLastName()),
                       jsonPath("$.dateOfBirth").value(patient.getDateOfBirth().toString()),
                       jsonPath("$.gender").value(patient.getGender().toString()),
                       jsonPath("$.role").value(patient.getRole().toString()),
                       jsonPath("$.doctor.email").value(doctor.getEmail())
               );
    }

    @Test
    void updateUserProfile_shouldUpdateProfile() throws Exception {
        PatientDTO profileDTO = new PatientDTO();
        profileDTO.setFirstName("UpdatedFirstName");
        profileDTO.setLastName("UpdatedLastName");
        profileDTO.setDateOfBirth(LocalDate.of(1990, 2, 1));
        profileDTO.setGender(Gender.F);
        profileDTO.setEmail(patient.getEmail());

        String json = objectMapper.writeValueAsString(profileDTO);

        mockMvc.perform(put("/user/profile").with(user(patient.getEmail()))
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(json))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.email").value(patient.getEmail()),
                       jsonPath("$.firstName").value("UpdatedFirstName"),
                       jsonPath("$.lastName").value("UpdatedLastName"),
                       jsonPath("$.dateOfBirth").value(LocalDate.of(1990, 2, 1).toString()),
                       jsonPath("$.gender").value(Gender.F.toString()),
                       jsonPath("$.role").value(patient.getRole().toString()),
                       jsonPath("$.doctor.email").value(doctor.getEmail())
        );
    }

    @Test
    void updateUserProfile_shouldReturn400_whenEmailAlreadyExists() throws Exception {
        PatientDTO profileDTO = new PatientDTO();
        profileDTO.setFirstName("UpdatedFirstName");
        profileDTO.setLastName("UpdatedLastName");
        profileDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        profileDTO.setGender(Gender.M);
        profileDTO.setEmail("doctor@example.com");

        String json = objectMapper.writeValueAsString(profileDTO);

        mockMvc.perform(put("/user/profile").with(user(patient.getEmail()))
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(json))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()),
                       jsonPath("$.error").value(HttpStatus.BAD_REQUEST.name()),
                       jsonPath("$.message").value("Invalid Format"),
                       jsonPath("$.fieldErrors.email").value("Cet email est déjà utilisé")
               );
    }

    @Test
    void updateUserProfile_shouldFail_whenInvalidForm() throws Exception {
        PatientDTO profileDTO = new PatientDTO();

        String json = objectMapper.writeValueAsString(profileDTO);

        mockMvc.perform(put("/user/profile").with(user(patient.getEmail()))
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(json))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()),
                       jsonPath("$.error").value(HttpStatus.BAD_REQUEST.name()),
                       jsonPath("$.message").value("Invalid Format"),
                       jsonPath("$.fieldErrors.firstName").value("Le prénom est obligatoire"),
                       jsonPath("$.fieldErrors.lastName").value("Le nom est obligatoire"),
                       jsonPath("$.fieldErrors.dateOfBirth").value("La date de naissance est obligatoire"),
                       jsonPath("$.fieldErrors.gender").value("Le genre est obligatoire"),
                       jsonPath("$.fieldErrors.email").value("L'email est obligatoire")
               );
    }

    @Test
    void updateUserPassword_shouldUpdatePassword() throws Exception {
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setCurrentPassword("OldPass123!");
        passwordDTO.setNewPassword("NewPass123!");
        passwordDTO.setConfirmPassword("NewPass123!");

        String json = objectMapper.writeValueAsString(passwordDTO);

        mockMvc.perform(put("/user/password").with(user(patient.getEmail()))
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .content(json))
               .andExpectAll(
                       status().isOk(),
                       content().string(containsString("Mot de passe mis à jour avec succès"))
               );
    }

    @Test
    void updateUserPassword_should400_whenInvalidForm() throws Exception {
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setCurrentPassword("WrongPass!");
        passwordDTO.setNewPassword("weak");
        passwordDTO.setConfirmPassword("Mismatch123!");

        String json = objectMapper.writeValueAsString(passwordDTO);

        mockMvc.perform(put("/user/password").with(user(patient.getEmail()))
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .content(json))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()),
                       jsonPath("$.error").value(HttpStatus.BAD_REQUEST.name()),
                       jsonPath("$.message").value("Invalid Format"),
                       jsonPath("$.fieldErrors.currentPassword").value("Mot de passe incorrecte"),
                       jsonPath("$.fieldErrors.newPassword").value("Format du mot de passe invalide"),
                       jsonPath("$.fieldErrors.confirmPassword").value("Les mots de passe sont différents")
               );
    }
}
*/
