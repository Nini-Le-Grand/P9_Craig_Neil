/*
package com.medilabo.users.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.medilabo.users.dto.PatientDTO;
import com.medilabo.users.dto.UserDTO;
import com.medilabo.users.domain.enums.Role;
import com.medilabo.users.entity.User;
import com.medilabo.users.repository.UserRepository;
import com.medilabo.users.utils.CreatePassword;
import com.medilabo.users.utils.DTOMapper;
import com.medilabo.users.utils.DTOValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AdminServiceTest {
    @Mock private UserService userService;
    @Mock private UserRepository userRepository;
    @Mock private DTOMapper dtoMapper;
    @Mock private DTOValidation dtoValidation;
    @Mock private CreatePassword createPassword;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private BindingResult bindingResult;
    @InjectMocks private AdminService adminService;
    private User user;
    private User doctor;
    private User patient;

    @BeforeEach
    void setup() {
        user = new User();
        user.setEmail("user@test.com");
        user.setRole(Role.PATIENT);

        doctor = new User();
        doctor.setEmail("doctor@test.com");
        doctor.setRole(Role.DOCTOR);

        patient = new User();
        patient.setEmail("patient@test.com");
        patient.setRole(Role.PATIENT);
    }

    @Test
    void getUserProfile_shouldReturnUserDTO() {
        when(userService.getUser(user.getEmail())).thenReturn(user);
        UserDTO userDTO = new UserDTO();
        when(dtoMapper.userToDTO(user)).thenReturn(userDTO);

        UserDTO result = adminService.getUserProfile(user.getEmail());

        assertSame(userDTO, result);
        verify(userService).getUser(user.getEmail());
        verify(dtoMapper).userToDTO(user);
    }

    @Test
    void findUsers_shouldReturnListOfUserDTO() {
        List<User> users = List.of(user, doctor);
        when(userRepository.searchByKeywordAndRole("test", null)).thenReturn(users);
        UserDTO userDTO1 = new UserDTO();
        UserDTO userDTO2 = new UserDTO();
        when(dtoMapper.userToDTO(user)).thenReturn(userDTO1);
        when(dtoMapper.userToDTO(doctor)).thenReturn(userDTO2);

        List<UserDTO> result = adminService.findUsers("test");

        assertEquals(2, result.size());
        assertTrue(result.contains(userDTO1));
        assertTrue(result.contains(userDTO2));
    }

    @Test
    void createUser_shouldValidateAndSaveUser() {
        PatientDTO profileDTO = new PatientDTO();
        doNothing().when(dtoValidation).validateProfileDto(null, profileDTO, bindingResult);
        User newUser = new User();
        when(dtoMapper.profileDtoToUser(any(User.class), eq(profileDTO))).thenReturn(newUser);
        when(createPassword.createDefaultPassword(newUser)).thenReturn("defaultPassword");
        when(passwordEncoder.encode("defaultPassword")).thenReturn("encodedPassword");
        UserDTO savedDTO = new UserDTO();
        when(userService.saveUser(newUser)).thenReturn(savedDTO);

        UserDTO result = adminService.createUser(profileDTO, bindingResult);

        assertEquals(savedDTO, result);
        assertEquals(Role.PATIENT, newUser.getRole());
        assertEquals("encodedPassword", newUser.getPassword());
    }

    @Test
    void updateUserProfile_shouldValidateAndSaveUser() {
        PatientDTO profileDTO = new PatientDTO();
        when(userService.getUser(user.getEmail())).thenReturn(user);
        doNothing().when(dtoValidation).validateProfileDto(user, profileDTO, bindingResult);
        when(dtoMapper.profileDtoToUser(user, profileDTO)).thenReturn(user);
        UserDTO savedDTO = new UserDTO();
        when(userService.saveUser(user)).thenReturn(savedDTO);

        UserDTO result = adminService.updateUserProfile(user.getEmail(), profileDTO, bindingResult);

        assertEquals(savedDTO, result);
    }

    @Test
    void updateUserRole_shouldUpdateRole_whenValid() {
        user.setDoctor(null);
        when(userService.getUser(user.getEmail())).thenReturn(user);
        when(userRepository.findByDoctorEmail(user.getEmail())).thenReturn(Collections.emptyList());
        UserDTO savedDTO = new UserDTO();
        when(userService.saveUser(user)).thenReturn(savedDTO);

        UserDTO result = adminService.updateUserRole(user.getEmail(), "DOCTOR");

        assertEquals(savedDTO, result);
        assertEquals(Role.DOCTOR, user.getRole());
    }

    @Test
    void updateUserRole_shouldThrowBadRequest_whenInvalidRole() {
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> adminService.updateUserRole(user.getEmail(), "INVALID_ROLE"));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Rôle invalide"));
    }

    @Test
    void updateUserRole_shouldThrowBadRequest_whenPatientHasDoctorAssigned() {
        user.setDoctor(doctor);
        when(userService.getUser(user.getEmail())).thenReturn(user);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> adminService.updateUserRole(user.getEmail(), "DOCTOR"));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Le patient a un médecin attribué"));
    }

    @Test
    void updateUserRole_shouldThrowBadRequest_whenDoctorHasPatients() {
        user.setRole(Role.DOCTOR);
        user.setDoctor(null);
        when(userService.getUser(user.getEmail())).thenReturn(user);
        when(userRepository.findByDoctorEmail(user.getEmail())).thenReturn(List.of(patient));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> adminService.updateUserRole(user.getEmail(), "PATIENT"));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Le médecin est attribué à des patients"));
    }

    @Test
    void assignDoctorToPatient_shouldAssignDoctorAndSave() {
        patient.setRole(Role.PATIENT);
        doctor.setRole(Role.DOCTOR);

        when(userService.getUser(patient.getEmail())).thenReturn(patient);
        when(userService.getUser(doctor.getEmail())).thenReturn(doctor);

        UserDTO savedDTO = new UserDTO();
        when(userService.saveUser(patient)).thenReturn(savedDTO);

        UserDTO result = adminService.assignDoctorToPatient(patient.getEmail(), doctor.getEmail());

        assertEquals(savedDTO, result);
        assertEquals(doctor, patient.getDoctor());
    }

    @Test
    void assignDoctorToPatient_shouldThrowBadRequest_whenUserIsNotPatient() {
        doctor.setRole(Role.DOCTOR);
        when(userService.getUser(doctor.getEmail())).thenReturn(doctor);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> adminService.assignDoctorToPatient(doctor.getEmail(), doctor.getEmail()));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Vous ne pouvez pas assigner de médecin"));
    }

    @Test
    void assignDoctorToPatient_shouldThrowBadRequest_whenDoctorIsNotDoctorRole() {
        patient.setRole(Role.PATIENT);
        when(userService.getUser(patient.getEmail())).thenReturn(patient);
        User notDoctor = new User();
        notDoctor.setRole(Role.PATIENT);
        when(userService.getUser("someDoctor@test.com")).thenReturn(notDoctor);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> adminService.assignDoctorToPatient(patient.getEmail(), "someDoctor@test.com"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("Vous ne pouvez pas assigner cet utilisateur"));
    }

    @Test
    void removeDoctorFromPatient_shouldRemoveDoctorAndSave() {
        patient.setRole(Role.PATIENT);
        patient.setDoctor(doctor);

        when(userService.getUser(patient.getEmail())).thenReturn(patient);
        UserDTO savedDTO = new UserDTO();
        when(userService.saveUser(patient)).thenReturn(savedDTO);

        UserDTO result = adminService.removeDoctorFromPatient(patient.getEmail());

        assertEquals(savedDTO, result);
        assertNull(patient.getDoctor());
    }

    @Test
    void removeDoctorFromPatient_shouldThrowBadRequest_whenUserIsNotPatient() {
        doctor.setRole(Role.DOCTOR);
        when(userService.getUser(doctor.getEmail())).thenReturn(doctor);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> adminService.removeDoctorFromPatient(doctor.getEmail()));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("n'est pas un patient"));
    }

    @Test
    void removeDoctorFromPatient_shouldThrowBadRequest_whenPatientHasNoDoctor() {
        patient.setRole(Role.PATIENT);
        patient.setDoctor(null);
        when(userService.getUser(patient.getEmail())).thenReturn(patient);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> adminService.removeDoctorFromPatient(patient.getEmail()));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getReason().contains("n'a pas de médecin assigné"));
    }

    @Test
    void resetUserPassword_shouldSetDefaultPassword() {
        when(userService.getUser(user.getEmail())).thenReturn(user);
        when(createPassword.createDefaultPassword(user)).thenReturn("defaultPassword");
        when(passwordEncoder.encode("defaultPassword")).thenReturn("encodedPassword");

        String response = adminService.resetUserPassword(user.getEmail());

        assertEquals("Mot de passe réinitialisé avec succès", response);
        assertEquals("encodedPassword", user.getPassword());
        verify(userService).saveUser(user);
    }
}
*/
