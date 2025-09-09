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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminControllerIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    private User admin;
    private User doctor1;
    private User doctor2;
    private User patient1;
    private User patient2;
    private User patient3;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        String rawPassword = "Valid123!";

        admin = new User();
        admin.setEmail("admin@test.com");
        admin.setFirstName("Admin");
        admin.setLastName("One");
        admin.setDateOfBirth(LocalDate.of(1990, 1, 1));
        admin.setGender(Gender.M);
        admin.setRole(Role.ADMIN);
        admin.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(admin);

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
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void getUser_shouldReturnUser() throws Exception {
        mockMvc.perform(get("/admin/user/{email}", patient1.getEmail()))
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

        mockMvc.perform(get("/admin/user/{email}", doctor1.getEmail()))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.email").value(doctor1.getEmail()),
                       jsonPath("$.firstName").value(doctor1.getFirstName()),
                       jsonPath("$.lastName").value(doctor1.getLastName()),
                       jsonPath("$.dateOfBirth").value(doctor1.getDateOfBirth().toString()),
                       jsonPath("$.gender").value(doctor1.getGender().toString()),
                       jsonPath("$.role").value(doctor1.getRole().toString()),
                       jsonPath("$.doctor").value(nullValue())
               );

        mockMvc.perform(get("/admin/user/{email}", admin.getEmail()))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.email").value(admin.getEmail()),
                       jsonPath("$.firstName").value(admin.getFirstName()),
                       jsonPath("$.lastName").value(admin.getLastName()),
                       jsonPath("$.dateOfBirth").value(admin.getDateOfBirth().toString()),
                       jsonPath("$.gender").value(admin.getGender().toString()),
                       jsonPath("$.role").value(admin.getRole().toString()),
                       jsonPath("$.doctor").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void getUser_shouldReturn404_userNotFound() throws Exception {
        mockMvc.perform(get("/admin/user/{email}", "unknown@email.com"))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()),
                       jsonPath("$.error").value(HttpStatus.NOT_FOUND.name()),
                       jsonPath("$.message").value("Utilisateur introuvable"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void searchUser_shouldReturnListOfUsers() throws Exception {
        doctor1.setFirstName("Alan");
        patient1.setLastName("alana");
        admin.setEmail("testalan@email.com");
        userRepository.save(doctor1);
        userRepository.save(patient1);
        userRepository.save(admin);

        mockMvc.perform(get("/admin/user/search?keyword={keyword}", "alan"))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.length()").value(3),
                       jsonPath("$[0].email").value(admin.getEmail()),
                       jsonPath("$[1].email").value(doctor1.getEmail()),
                       jsonPath("$[2].email").value(patient1.getEmail()),
                       jsonPath("$[2].doctor.email").value(doctor1.getEmail())
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void searchUser_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/admin/user/search?keyword={keyword}", "unknwon"))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.length()").value(0)
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void createUser_shouldReturnCreatedPatient() throws Exception {
        PatientDTO profileDTO = new PatientDTO();
        profileDTO.setEmail("new@test.com");
        profileDTO.setFirstName("Patient");
        profileDTO.setLastName("New");
        profileDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        profileDTO.setGender(Gender.M);

        String json = objectMapper.writeValueAsString(profileDTO);

        mockMvc.perform(post("/admin/user")
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
                       jsonPath("$.doctor").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void createUser_shouldReturn400_InvalidForm() throws Exception {
        PatientDTO profileDTO = new PatientDTO();
        profileDTO.setEmail(patient1.getEmail());

        String json = objectMapper.writeValueAsString(profileDTO);

        mockMvc.perform(post("/admin/user")
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
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void updateUserProfile_shouldReturnUpdatedUser() throws Exception {
        PatientDTO profileDTO = new PatientDTO();
        profileDTO.setEmail("new@test.com");
        profileDTO.setFirstName("Doctor");
        profileDTO.setLastName("New");
        profileDTO.setDateOfBirth(LocalDate.of(1990, 2, 1));
        profileDTO.setGender(Gender.F);

        String json = objectMapper.writeValueAsString(profileDTO);

        mockMvc.perform(put("/admin/user/{email}", doctor1.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.email").value(profileDTO.getEmail()),
                       jsonPath("$.firstName").value(profileDTO.getFirstName()),
                       jsonPath("$.lastName").value(profileDTO.getLastName()),
                       jsonPath("$.dateOfBirth").value(profileDTO.getDateOfBirth().toString()),
                       jsonPath("$.gender").value(profileDTO.getGender().toString()),
                       jsonPath("$.role").value(Role.DOCTOR.toString()),
                       jsonPath("$.doctor").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void updateUserProfile_shouldReturn404_userNotFound() throws Exception {
        PatientDTO profileDTO = new PatientDTO();

        String json = objectMapper.writeValueAsString(profileDTO);

        mockMvc.perform(put("/admin/user/{email}", "unknown@email.com")
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
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void updateUserProfile_shouldReturn400_InvalidForm() throws Exception {
        PatientDTO profileDTO = new PatientDTO();
        profileDTO.setEmail(patient1.getEmail());

        String json = objectMapper.writeValueAsString(profileDTO);

        mockMvc.perform(put("/admin/user/{email}", doctor1.getEmail())
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
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void updateUserRole_shouldReturnUpdatedUser() throws Exception {
        patient1.setDoctor(null);
        userRepository.save(patient1);

        mockMvc.perform(put("/admin/user/role/{email}", patient1.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("doctor"))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.email").value(patient1.getEmail()),
                       jsonPath("$.firstName").value(patient1.getFirstName()),
                       jsonPath("$.lastName").value(patient1.getLastName()),
                       jsonPath("$.dateOfBirth").value(patient1.getDateOfBirth().toString()),
                       jsonPath("$.gender").value(patient1.getGender().toString()),
                       jsonPath("$.role").value(Role.DOCTOR.toString()),
                       jsonPath("$.doctor").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void updateUserRole_shouldReturn400_InvalidRole() throws Exception {
        mockMvc.perform(put("/admin/user/role/{email}", patient1.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("other"))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()),
                       jsonPath("$.error").value(HttpStatus.BAD_REQUEST.name()),
                       jsonPath("$.message").value("Rôle invalide : other"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void updateUserRole_shouldReturn404_userNotFound() throws Exception {
        mockMvc.perform(put("/admin/user/role/{email}", "unknown@test.com")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("PATIENT"))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()),
                       jsonPath("$.error").value(HttpStatus.NOT_FOUND.name()),
                       jsonPath("$.message").value("Utilisateur introuvable"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void updateUserRole_shouldReturn400_userHasAssignedDoctor() throws Exception {
        mockMvc.perform(put("/admin/user/role/{email}", patient1.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("doctor"))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()),
                       jsonPath("$.error").value(HttpStatus.BAD_REQUEST.name()),
                       jsonPath("$.message").value("Le patient a un médecin attribué"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void updateUserRole_shouldReturn400_userIsAssignedToPatients() throws Exception {
        mockMvc.perform(put("/admin/user/role/{email}", doctor1.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("patient"))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()),
                       jsonPath("$.error").value(HttpStatus.BAD_REQUEST.name()),
                       jsonPath("$.message").value("Le médecin est attribué à des patients"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void assignDoctorToPatient_shouldReturnUpdatedUser() throws Exception {
        patient1.setDoctor(null);
        userRepository.save(patient1);

        mockMvc.perform(put("/admin/user/assignDoctor/{email}", patient1.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("doctor1@test.com"))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.email").value(patient1.getEmail()),
                       jsonPath("$.firstName").value(patient1.getFirstName()),
                       jsonPath("$.lastName").value(patient1.getLastName()),
                       jsonPath("$.dateOfBirth").value(patient1.getDateOfBirth().toString()),
                       jsonPath("$.gender").value(patient1.getGender().toString()),
                       jsonPath("$.role").value(Role.PATIENT.toString()),
                       jsonPath("$.doctor.email").value(doctor1.getEmail())
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void assignDoctorToPatient_alreadyAssigned_shouldReturnUpdatedUser() throws Exception {
        mockMvc.perform(put("/admin/user/assignDoctor/{email}", patient1.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("doctor2@test.com"))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.email").value(patient1.getEmail()),
                       jsonPath("$.firstName").value(patient1.getFirstName()),
                       jsonPath("$.lastName").value(patient1.getLastName()),
                       jsonPath("$.dateOfBirth").value(patient1.getDateOfBirth().toString()),
                       jsonPath("$.gender").value(patient1.getGender().toString()),
                       jsonPath("$.role").value(Role.PATIENT.toString()),
                       jsonPath("$.doctor.email").value(doctor2.getEmail())
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void assignDoctorToPatient_shouldReturn404_patientNotFound() throws Exception {
        mockMvc.perform(put("/admin/user/assignDoctor/{email}", "unknown@test.com")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("doctor2@test.com"))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()),
                       jsonPath("$.error").value(HttpStatus.NOT_FOUND.name()),
                       jsonPath("$.message").value("Utilisateur introuvable"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void assignDoctorToPatient_shouldReturn401_patientIsNotPatient() throws Exception {
        mockMvc.perform(put("/admin/user/assignDoctor/{email}", doctor2.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("doctor1@test.com"))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()),
                       jsonPath("$.error").value(HttpStatus.BAD_REQUEST.name()),
                       jsonPath("$.message").value("Vous ne pouvez pas assigner de médecin à cet utilisateur"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void assignDoctorToPatient_shouldReturn404_doctorNotFound() throws Exception {
        mockMvc.perform(put("/admin/user/assignDoctor/{email}", patient1.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("unknown@test.com"))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()),
                       jsonPath("$.error").value(HttpStatus.NOT_FOUND.name()),
                       jsonPath("$.message").value("Utilisateur introuvable"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void assignDoctorToPatient_shouldReturn400_doctorIsNotDoctor() throws Exception {
        mockMvc.perform(put("/admin/user/assignDoctor/{email}", patient1.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(patient2.getEmail()))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()),
                       jsonPath("$.error").value(HttpStatus.BAD_REQUEST.name()),
                       jsonPath("$.message").value("Vous ne pouvez pas assigner cet utilisateur à un patient"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void removeDoctorFromPatient_shouldReturnUpdatedUser() throws Exception {
        mockMvc.perform(put("/admin/user/removeDoctor/{email}", patient1.getEmail()))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.email").value(patient1.getEmail()),
                       jsonPath("$.firstName").value(patient1.getFirstName()),
                       jsonPath("$.lastName").value(patient1.getLastName()),
                       jsonPath("$.dateOfBirth").value(patient1.getDateOfBirth().toString()),
                       jsonPath("$.gender").value(patient1.getGender().toString()),
                       jsonPath("$.role").value(Role.PATIENT.toString()),
                       jsonPath("$.doctor").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void removeDoctorFromPatient_shouldReturn404_patientNotFound() throws Exception {
        mockMvc.perform(put("/admin/user/removeDoctor/{email}", "unknown@test.com"))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()),
                       jsonPath("$.error").value(HttpStatus.NOT_FOUND.name()),
                       jsonPath("$.message").value("Utilisateur introuvable"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void removeDoctorFromPatient_shouldReturn401_patientIsNotPatient() throws Exception {
        mockMvc.perform(put("/admin/user/removeDoctor/{email}", doctor2.getEmail()))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()),
                       jsonPath("$.error").value(HttpStatus.BAD_REQUEST.name()),
                       jsonPath("$.message").value("Cet utilisateur n'est pas un patient"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void removeDoctorFromPatient_shouldReturn40_patientHasNoDoctorAssigned() throws Exception {
        patient1.setDoctor(null);
        userRepository.save(patient1);

        mockMvc.perform(put("/admin/user/removeDoctor/{email}", patient1.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("unknown@test.com"))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()),
                       jsonPath("$.error").value(HttpStatus.BAD_REQUEST.name()),
                       jsonPath("$.message").value("Ce patient n'a pas de médecin assigné"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void resetUserPassword_shouldReturnConfirmationMessage() throws Exception {
        mockMvc.perform(post("/admin/user/password/{email}", patient1.getEmail()))
               .andExpectAll(
                       status().isOk(),
                       content().string("Mot de passe réinitialisé avec succès")
               );
    }

    @Test
    @WithMockUser(username = "admin@test.com", roles = "ADMIN")
    void resetUserPassword_shouldReturn404_patientNotFound() throws Exception {
        mockMvc.perform(post("/admin/user/password/{email}", "unknown@test.com"))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()),
                       jsonPath("$.error").value(HttpStatus.NOT_FOUND.name()),
                       jsonPath("$.message").value("Utilisateur introuvable"),
                       jsonPath("$.fieldErrors").value(nullValue())
               );
    }
}
*/
