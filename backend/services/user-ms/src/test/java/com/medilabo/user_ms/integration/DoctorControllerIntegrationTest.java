/*
package com.medilabo.users.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DoctorControllerIntegrationTest {
   @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    private User doctor1;
    private User doctor2;
    private User patient1;
    private User patient2;
    private User patient3;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        String rawPassword = "Valid123!";

        doctor1 = new User();
        doctor1.setEmail("doctor1@test.com");
        doctor1.setFirstName("Doctor");
        doctor1.setLastName("One");
        doctor1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        doctor1.setGender(Gender.M);
        doctor1.setRole(Role.DOCTOR);
        doctor1.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(doctor1);

        doctor2 = new User();
        doctor2.setEmail("doctor2@test.com");
        doctor2.setFirstName("Doctor");
        doctor2.setLastName("Two");
        doctor2.setDateOfBirth(LocalDate.of(1990, 1, 1));
        doctor2.setGender(Gender.M);
        doctor2.setRole(Role.DOCTOR);
        doctor2.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(doctor2);

        patient1 = new User();
        patient1.setEmail("patient1@test.com");
        patient1.setFirstName("Patient");
        patient1.setLastName("One");
        patient1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient1.setGender(Gender.F);
        patient1.setRole(Role.PATIENT);
        patient1.setDoctor(doctor1);
        patient1.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(patient1);

        patient2 = new User();
        patient2.setEmail("patient2@test.com");
        patient2.setFirstName("Patient");
        patient2.setLastName("Two");
        patient2.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient2.setGender(Gender.F);
        patient2.setRole(Role.PATIENT);
        patient2.setDoctor(doctor1);
        patient2.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(patient2);

        patient3 = new User();
        patient3.setEmail("patient3@test.com");
        patient3.setFirstName("Patient");
        patient3.setLastName("Three");
        patient3.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient3.setGender(Gender.F);
        patient3.setRole(Role.PATIENT);
        patient3.setDoctor(doctor1);
        patient3.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(patient3);
    }

    @Test
    @WithMockUser(username = "doctor1@test.com", roles = "DOCTOR")
    void getPatient_shouldReturnPatient() throws Exception {
        mockMvc.perform(get("/doctor/patient/{email}", patient1.getEmail()))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.email").value(patient1.getEmail()),
                       jsonPath("$.firstName").value(patient1.getFirstName()),
                       jsonPath("$.lastName").value(patient1.getLastName()),
                       jsonPath("$.dateOfBirth").value(patient1.getDateOfBirth().toString()),
                       jsonPath("$.gender").value(patient1.getGender().toString()),
                       jsonPath("$.role").value(patient1.getRole().toString()),
                       jsonPath("$.doctor.email").value(doctor1.getEmail())
               );
    }

    @Test
    @WithMockUser(username = "doctor1@test.com", roles = "DOCTOR")
    void getPatient_shouldReturn404_unknownUser() throws Exception {
        mockMvc.perform(get("/doctor/patient/{email}", "unknown@email.com"))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()),
                       jsonPath("$.error").value(HttpStatus.NOT_FOUND.name()),
                       jsonPath("$.message").value("Utilisateur introuvable"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "doctor1@test.com", roles = "DOCTOR")
    void getPatient_shouldReturn403_userIsNotPatient() throws Exception {
        mockMvc.perform(get("/doctor/patient/{email}", doctor1.getEmail()))
               .andExpectAll(
                       status().isForbidden(),
                       jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()),
                       jsonPath("$.error").value(HttpStatus.FORBIDDEN.name()),
                       jsonPath("$.message").value("Vous n'êtes pas autorisés à consulter cet utilisateur"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "doctor1@test.com", roles = "DOCTOR")
    void getPatients_shouldReturnPatients() throws Exception {
        patient3.setDoctor(doctor2);
        userRepository.save(patient3);

        mockMvc.perform(get("/doctor/patients"))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.length()").value(2),
                       jsonPath("$[0].email").value(patient1.getEmail()),
                       jsonPath("$[1].email").value(patient2.getEmail())
               );
    }

    @Test
    @WithMockUser(username = "doctor1@test.com", roles = "DOCTOR")
    void createPatient_shouldReturnCreatedPatient() throws Exception {
        PatientDTO profileDTO = new PatientDTO();
        profileDTO.setEmail("new@test.com");
        profileDTO.setFirstName("Patient");
        profileDTO.setLastName("New");
        profileDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        profileDTO.setGender(Gender.M);

        String json = objectMapper.writeValueAsString(profileDTO);

        mockMvc.perform(post("/doctor/patient")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(json))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.email").value(profileDTO.getEmail()),
                       jsonPath("$.firstName").value(profileDTO.getFirstName()),
                       jsonPath("$.lastName").value(profileDTO.getLastName()),
                       jsonPath("$.dateOfBirth").value(profileDTO.getDateOfBirth().toString()),
                       jsonPath("$.gender").value(profileDTO.getGender().toString()),
                       jsonPath("$.role").value(Role.PATIENT.toString()),
                       jsonPath("$.doctor.email").value(doctor1.getEmail())
               );
    }

    @Test
    @WithMockUser(username = "doctor1@test.com", roles = "DOCTOR")
    void createPatient_shouldReturn400_InvalidForm() throws Exception {
        PatientDTO profileDTO = new PatientDTO();
        profileDTO.setEmail(patient1.getEmail());

        String json = objectMapper.writeValueAsString(profileDTO);

        mockMvc.perform(post("/doctor/patient")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()),
                       jsonPath("$.error").value(HttpStatus.BAD_REQUEST.name()),
                       jsonPath("$.message").value("Invalid Format"),
                       jsonPath("$.fieldErrors.email").value("Cet email est déjà utilisé"),
                       jsonPath("$.fieldErrors.firstName").value("Le prénom est obligatoire"),
                       jsonPath("$.fieldErrors.lastName").value("Le nom est obligatoire"),
                       jsonPath("$.fieldErrors.dateOfBirth").value("La date de naissance est obligatoire"),
                       jsonPath("$.fieldErrors.gender").value("Le genre est obligatoire")
               );
    }

    @Test
    @WithMockUser(username = "doctor1@test.com", roles = "DOCTOR")
    void updatePatient_shouldReturnUpdatedPatient() throws Exception {
        PatientDTO profileDTO = new PatientDTO();
        profileDTO.setEmail(patient1.getEmail());
        profileDTO.setFirstName("Patient");
        profileDTO.setLastName("New");
        profileDTO.setDateOfBirth(LocalDate.of(1990, 2, 1));
        profileDTO.setGender(Gender.F);

        String json = objectMapper.writeValueAsString(profileDTO);

        mockMvc.perform(put("/doctor/patient/{email}", patient1.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.email").value(profileDTO.getEmail()),
                       jsonPath("$.firstName").value(profileDTO.getFirstName()),
                       jsonPath("$.lastName").value(profileDTO.getLastName()),
                       jsonPath("$.dateOfBirth").value(profileDTO.getDateOfBirth().toString()),
                       jsonPath("$.gender").value(profileDTO.getGender().toString()),
                       jsonPath("$.role").value(Role.PATIENT.toString()),
                       jsonPath("$.doctor.email").value(doctor1.getEmail())
               );
    }

    @Test
    @WithMockUser(username = "doctor1@test.com", roles = "DOCTOR")
    void updatePatient_shouldReturn404_NotFound() throws Exception {
        PatientDTO profileDTO = new PatientDTO();

        String json = objectMapper.writeValueAsString(profileDTO);

        mockMvc.perform(put("/doctor/patient/{email}", "unknown@test.com")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()),
                       jsonPath("$.error").value(HttpStatus.NOT_FOUND.name()),
                       jsonPath("$.message").value("Utilisateur introuvable"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "doctor1@test.com", roles = "DOCTOR")
    void updatePatient_shouldReturn400_userNotPatient() throws Exception {
        PatientDTO profileDTO = new PatientDTO();

        String json = objectMapper.writeValueAsString(profileDTO);

        mockMvc.perform(put("/doctor/patient/{email}", doctor2.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpectAll(
                       status().isForbidden(),
                       jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()),
                       jsonPath("$.error").value(HttpStatus.FORBIDDEN.name()),
                       jsonPath("$.message").value("Vous n'êtes pas autorisés à modifier cet utilisateur"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "doctor2@test.com", roles = "DOCTOR")
    void updatePatient_shouldReturn403_userNotAssignedToDoctor() throws Exception {
        PatientDTO profileDTO = new PatientDTO();

        String json = objectMapper.writeValueAsString(profileDTO);

        mockMvc.perform(put("/doctor/patient/{email}", patient1.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpectAll(
                       status().isForbidden(),
                       jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()),
                       jsonPath("$.error").value(HttpStatus.FORBIDDEN.name()),
                       jsonPath("$.message").value("Vous n'êtes pas autorisés à modifier cet utilisateur"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "doctor1@test.com", roles = "DOCTOR")
    void updatePatient_shouldReturn400_InvalidForm() throws Exception {
        PatientDTO profileDTO = new PatientDTO();
        profileDTO.setEmail(patient2.getEmail());

        String json = objectMapper.writeValueAsString(profileDTO);

        mockMvc.perform(put("/doctor/patient/{email}", patient1.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()),
                       jsonPath("$.error").value(HttpStatus.BAD_REQUEST.name()),
                       jsonPath("$.message").value("Invalid Format"),
                       jsonPath("$.fieldErrors.email").value("Cet email est déjà utilisé"),
                       jsonPath("$.fieldErrors.firstName").value("Le prénom est obligatoire"),
                       jsonPath("$.fieldErrors.lastName").value("Le nom est obligatoire"),
                       jsonPath("$.fieldErrors.dateOfBirth").value("La date de naissance est obligatoire"),
                       jsonPath("$.fieldErrors.gender").value("Le genre est obligatoire")
               );
    }

    @Test
    @WithMockUser(username = "doctor2@test.com", roles = "DOCTOR")
    void assignDoctorToPatient_shouldReturnUpdatedPatient() throws Exception {
        patient1.setDoctor(null);
        userRepository.save(patient1);

        mockMvc.perform(put("/doctor/patient/assign/{email}", patient1.getEmail()))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.email").value(patient1.getEmail()),
                       jsonPath("$.firstName").value(patient1.getFirstName()),
                       jsonPath("$.lastName").value(patient1.getLastName()),
                       jsonPath("$.dateOfBirth").value(patient1.getDateOfBirth().toString()),
                       jsonPath("$.gender").value(patient1.getGender().toString()),
                       jsonPath("$.role").value(patient1.getRole().toString()),
                       jsonPath("$.doctor.email").value(doctor2.getEmail())
               );
    }

    @Test
    @WithMockUser(username = "doctor2@test.com", roles = "DOCTOR")
    void assignDoctorToPatient_shouldReturn404_notFound() throws Exception {
        mockMvc.perform(put("/doctor/patient/assign/{email}", "unknown@test.com"))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()),
                       jsonPath("$.error").value(HttpStatus.NOT_FOUND.name()),
                       jsonPath("$.message").value("Utilisateur introuvable"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "doctor1@test.com", roles = "DOCTOR")
    void assignDoctorToPatient_shouldReturn400_notPatient() throws Exception {
        mockMvc.perform(put("/doctor/patient/assign/{email}", doctor2.getEmail()))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()),
                       jsonPath("$.error").value(HttpStatus.BAD_REQUEST.name()),
                       jsonPath("$.message").value("Cet utilisateur n'est pas un patient"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "doctor2@test.com", roles = "DOCTOR")
    void assignDoctorToPatient_shouldReturn403_alreadyAssigned() throws Exception {
        mockMvc.perform(put("/doctor/patient/assign/{email}", patient1.getEmail()))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()),
                       jsonPath("$.error").value(HttpStatus.BAD_REQUEST.name()),
                       jsonPath("$.message").value("Un médecin est déjà assigné à l'utilisateur"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "doctor1@test.com", roles = "DOCTOR")
    void removeDoctorFromPatient_shouldReturnUpdatedPatient() throws Exception {
        mockMvc.perform(put("/doctor/patient/remove/{email}", patient1.getEmail()))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.email").value(patient1.getEmail()),
                       jsonPath("$.firstName").value(patient1.getFirstName()),
                       jsonPath("$.lastName").value(patient1.getLastName()),
                       jsonPath("$.dateOfBirth").value(patient1.getDateOfBirth().toString()),
                       jsonPath("$.gender").value(patient1.getGender().toString()),
                       jsonPath("$.role").value(patient1.getRole().toString()),
                       jsonPath("$.doctor").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "doctor2@test.com", roles = "DOCTOR")
    void removeDoctorFromPatient_shouldReturn404_notFound() throws Exception {
        mockMvc.perform(put("/doctor/patient/remove/{email}", "unknown@test.com"))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()),
                       jsonPath("$.error").value(HttpStatus.NOT_FOUND.name()),
                       jsonPath("$.message").value("Utilisateur introuvable"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "doctor1@test.com", roles = "DOCTOR")
    void removeDoctorFromPatient_shouldReturn400_notPatient() throws Exception {
        mockMvc.perform(put("/doctor/patient/remove/{email}", doctor2.getEmail()))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()),
                       jsonPath("$.error").value(HttpStatus.BAD_REQUEST.name()),
                       jsonPath("$.message").value("Cet utilisateur n'est pas un patient"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "doctor1@test.com", roles = "DOCTOR")
    void removeDoctorFromPatient_shouldReturn400_noDoctorAssigned() throws Exception {
        patient1.setDoctor(null);
        userRepository.save(patient1);

        mockMvc.perform(put("/doctor/patient/remove/{email}", patient1.getEmail()))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()),
                       jsonPath("$.error").value(HttpStatus.BAD_REQUEST.name()),
                       jsonPath("$.message").value("Ce patient n'a pas de médecin assigné"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "doctor2@test.com", roles = "DOCTOR")
    void removeDoctorFromPatient_shouldReturn403_doctorIsNotTheOneAssigned() throws Exception {
        mockMvc.perform(put("/doctor/patient/remove/{email}", patient1.getEmail()))
               .andExpectAll(
                       status().isForbidden(),
                       jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()),
                       jsonPath("$.error").value(HttpStatus.FORBIDDEN.name()),
                       jsonPath("$.message").value("Vous n'êtes pas autorisés à modifier cet utilisateur"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "doctor1@test.com", roles = "DOCTOR")
    void searchPatients_shouldReturnListOfPatients() throws Exception {
        patient1.setFirstName("Alan");
        patient1.setDoctor(doctor1);
        patient2.setLastName("Alana");
        patient2.setDoctor(doctor2);
        patient3.setEmail("alan@test.com");
        patient3.setDoctor(null);
        doctor1.setFirstName("Alan");
        doctor2.setLastName("Alan");
        userRepository.save(patient1);
        userRepository.save(patient2);
        userRepository.save(patient3);
        userRepository.save(doctor1);
        userRepository.save(doctor2);

        mockMvc.perform(get("/doctor/patient/search?keyword={keyword}", "alan"))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.length()").value(3),
                       jsonPath("$[0].email").value(patient1.getEmail()),
                       jsonPath("$[0].doctor.email").value(doctor1.getEmail()),
                       jsonPath("$[1].email").value(patient2.getEmail()),
                       jsonPath("$[1].doctor.email").value(doctor2.getEmail()),
                       jsonPath("$[2].email").value(patient3.getEmail()),
                       jsonPath("$[2].doctor").value(nullValue())
               );

        mockMvc.perform(get("/doctor/patient/search?keyword={keyword}", "alana"))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.length()").value(1),
                       jsonPath("$[0].email").value(patient2.getEmail())
               );
    }

    @Test
    @WithMockUser(username = "doctor1@test.com", roles = "DOCTOR")
    void searchPatients_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/doctor/patient/search?keyword={keyword}", "unknown"))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.length()").value(0)
               );
    }
}
*/
