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
class DoctorServiceTest {
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserService userService;
    @Mock private UserRepository userRepository;
    @Mock private DTOMapper dtoMapper;
    @Mock private DTOValidation dtoValidation;
    @Mock private CreatePassword createPassword;
    @Mock private BindingResult bindingResult;
    @InjectMocks private DoctorService doctorService;
    private User doctor;
    private User patient;

    @BeforeEach
    void setUp() {
        doctor = new User();
        doctor.setEmail("doc@test.com");
        doctor.setRole(Role.DOCTOR);

        patient = new User();
        patient.setEmail("patient@test.com");
        patient.setRole(Role.PATIENT);
        patient.setDoctor(doctor);
    }

    @Test
    void getPatientProfile_shouldReturnUserDTO_whenPatientRole() {
        when(userService.getUser(patient.getEmail())).thenReturn(patient);
        UserDTO userDTO = new UserDTO();
        when(dtoMapper.userToDTO(patient)).thenReturn(userDTO);

        UserDTO result = doctorService.getPatientProfile(patient.getEmail());

        assertSame(userDTO, result);
        verify(userService).getUser(patient.getEmail());
        verify(dtoMapper).userToDTO(patient);
    }

    @Test
    void getPatientProfile_shouldThrowForbidden_whenNotPatient() {
        User user = new User();
        user.setRole(Role.DOCTOR);
        when(userService.getUser(user.getEmail())).thenReturn(user);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> doctorService.getPatientProfile(user.getEmail()));

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
    }

    @Test
    void getPatients_shouldReturnListOfUserDTO() {
        when(userService.getConnectedUser()).thenReturn(doctor);
        when(userRepository.findByDoctorEmail(doctor.getEmail())).thenReturn(Collections.singletonList(patient));
        UserDTO userDTO = new UserDTO();
        when(dtoMapper.userToDTO(patient)).thenReturn(userDTO);

        List<UserDTO> result = doctorService.getPatients();

        assertEquals(1, result.size());
        assertEquals(userDTO, result.get(0));
    }

    @Test
    void createPatient_shouldSaveAndReturnUserDTO() {
        PatientDTO profileDTO = new PatientDTO();
        doNothing().when(dtoValidation).validateProfileDto(null, profileDTO, bindingResult);
        User newPatient = new User();
        when(dtoMapper.profileDtoToUser(any(User.class), eq(profileDTO))).thenReturn(newPatient);
        when(createPassword.createDefaultPassword(newPatient)).thenReturn("defaultPassword");
        when(passwordEncoder.encode("defaultPassword")).thenReturn("encodedPassword");
        when(userService.getConnectedUser()).thenReturn(doctor);
        UserDTO savedDTO = new UserDTO();
        when(userService.saveUser(newPatient)).thenReturn(savedDTO);

        UserDTO result = doctorService.createPatient(profileDTO, bindingResult);

        assertEquals(savedDTO, result);
        assertEquals(Role.PATIENT, newPatient.getRole());
        assertEquals("encodedPassword", newPatient.getPassword());
        assertEquals(doctor, newPatient.getDoctor());
    }

    @Test
    void updatePatientProfile_shouldUpdateAndReturnUserDTO() {
        PatientDTO profileDTO = new PatientDTO();
        when(userService.getUser(patient.getEmail())).thenReturn(patient);
        when(userService.getConnectedUser()).thenReturn(doctor);
        doNothing().when(dtoValidation).validateProfileDto(patient, profileDTO, bindingResult);
        when(dtoMapper.profileDtoToUser(patient, profileDTO)).thenReturn(patient);
        UserDTO savedDTO = new UserDTO();
        when(userService.saveUser(patient)).thenReturn(savedDTO);

        UserDTO result = doctorService.updatePatientProfile(patient.getEmail(), profileDTO, bindingResult);

        assertEquals(savedDTO, result);
    }

    @Test
    void updatePatientProfile_shouldThrowForbidden_ifUserNotPatient() {
        User notPatient = new User();
        notPatient.setRole(Role.DOCTOR);
        when(userService.getUser(notPatient.getEmail())).thenReturn(notPatient);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> doctorService.updatePatientProfile(notPatient.getEmail(), new PatientDTO(), bindingResult));

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
    }

    @Test
    void updatePatientProfile_shouldThrowForbidden_ifDoctorMismatch() {
        User patientWithAnotherDoctor = new User();
        patientWithAnotherDoctor.setRole(Role.PATIENT);
        patientWithAnotherDoctor.setDoctor(new User());

        when(userService.getUser(patientWithAnotherDoctor.getEmail())).thenReturn(patientWithAnotherDoctor);
        when(userService.getConnectedUser()).thenReturn(doctor);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> doctorService.updatePatientProfile(patientWithAnotherDoctor.getEmail(), new PatientDTO(), bindingResult));

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
    }

    @Test
    void assignDoctorToPatient_shouldAssignDoctorAndSave() {
        User patientWithoutDoctor = new User();
        patientWithoutDoctor.setRole(Role.PATIENT);
        patientWithoutDoctor.setDoctor(null);

        when(userService.getUser(patientWithoutDoctor.getEmail())).thenReturn(patientWithoutDoctor);
        when(userService.getConnectedUser()).thenReturn(doctor);
        UserDTO savedDTO = new UserDTO();
        when(userService.saveUser(patientWithoutDoctor)).thenReturn(savedDTO);

        UserDTO result = doctorService.assignDoctorToPatient(patientWithoutDoctor.getEmail());

        assertEquals(savedDTO, result);
        assertEquals(doctor, patientWithoutDoctor.getDoctor());
    }

    @Test
    void assignDoctorToPatient_shouldThrowBadRequest_ifNotPatient() {
        User notPatient = new User();
        notPatient.setRole(Role.DOCTOR);
        when(userService.getUser(notPatient.getEmail())).thenReturn(notPatient);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> doctorService.assignDoctorToPatient(notPatient.getEmail()));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void assignDoctorToPatient_shouldThrowBadRequest_ifDoctorAlreadyAssigned() {
        patient.setDoctor(doctor);
        when(userService.getUser(patient.getEmail())).thenReturn(patient);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> doctorService.assignDoctorToPatient(patient.getEmail()));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void removeDoctorFromPatient_shouldRemoveDoctorAndSave() {
        when(userService.getUser(patient.getEmail())).thenReturn(patient);
        when(userService.getConnectedUser()).thenReturn(doctor);
        UserDTO savedDTO = new UserDTO();
        when(userService.saveUser(patient)).thenReturn(savedDTO);

        UserDTO result = doctorService.removeDoctorFromPatient(patient.getEmail());

        assertEquals(savedDTO, result);
        assertNull(patient.getDoctor());
    }

    @Test
    void removeDoctorFromPatient_shouldThrowBadRequest_ifNotPatient() {
        User notPatient = new User();
        notPatient.setRole(Role.DOCTOR);
        when(userService.getUser(notPatient.getEmail())).thenReturn(notPatient);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> doctorService.removeDoctorFromPatient(notPatient.getEmail()));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void removeDoctorFromPatient_shouldThrowBadRequest_ifNoDoctorAssigned() {
        User patientNoDoctor = new User();
        patientNoDoctor.setRole(Role.PATIENT);
        patientNoDoctor.setDoctor(null);
        when(userService.getUser(patientNoDoctor.getEmail())).thenReturn(patientNoDoctor);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> doctorService.removeDoctorFromPatient(patientNoDoctor.getEmail()));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void removeDoctorFromPatient_shouldThrowForbidden_ifDoctorMismatch() {
        User patientWithOtherDoctor = new User();
        patientWithOtherDoctor.setRole(Role.PATIENT);
        patientWithOtherDoctor.setDoctor(new User());

        when(userService.getUser(patientWithOtherDoctor.getEmail())).thenReturn(patientWithOtherDoctor);
        when(userService.getConnectedUser()).thenReturn(doctor);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> doctorService.removeDoctorFromPatient(patientWithOtherDoctor.getEmail()));

        assertEquals(HttpStatus.FORBIDDEN, ex.getStatusCode());
    }

    @Test
    void findPatients_shouldReturnMappedList() {
        String keyword = "keyword";
        List<User> patients = Collections.singletonList(patient);
        when(userRepository.searchByKeywordAndRole(keyword, Role.PATIENT)).thenReturn(patients);
        UserDTO userDTO = new UserDTO();
        when(dtoMapper.userToDTO(patient)).thenReturn(userDTO);

        List<UserDTO> result = doctorService.findPatients(keyword);

        assertEquals(1, result.size());
        assertEquals(userDTO, result.get(0));
    }
}
*/
