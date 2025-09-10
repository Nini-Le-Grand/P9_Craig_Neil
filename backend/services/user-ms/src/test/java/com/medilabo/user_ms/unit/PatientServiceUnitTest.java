package com.medilabo.user_ms.unit;

import com.medilabo.user_ms.domain.dto.PatientDTO;
import com.medilabo.user_ms.domain.entity.Patient;
import com.medilabo.user_ms.domain.entity.User;
import com.medilabo.user_ms.repository.PatientRepository;
import com.medilabo.user_ms.service.PatientService;
import com.medilabo.user_ms.service.UserService;
import com.medilabo.user_ms.utils.DTOMapper;
import com.medilabo.user_ms.utils.DTOValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceUnitTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserService userService;

    @Mock
    private DTOValidation dtoValidation;

    @Mock
    private DTOMapper dtoMapper;

    @InjectMocks
    private PatientService patientService;

    private User doctor;
    private Patient patient;
    private PatientDTO patientDTO;
    private BindingResult bindingResult;

    @BeforeEach
    void setup() {
        doctor = new User();
        doctor.setId("d1");

        patient = new Patient();
        patient.setId("p1");
        patient.setDoctor(doctor);

        patientDTO = new PatientDTO();
        patientDTO.setDoctorId(doctor.getId());
        patientDTO.setEmail("patient@example.com");

        bindingResult = new BeanPropertyBindingResult(patientDTO, "patientDTO");
    }

    @Test
    void getPatientById_found() {
        when(patientRepository.findById("p1")).thenReturn(Optional.of(patient));

        Patient result = patientService.getPatientById("p1");
        assertEquals(patient, result);
    }

    @Test
    void getPatientById_notFound_throwsException() {
        when(patientRepository.findById("p1")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> patientService.getPatientById("p1"));
        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void getPatients_returnsList() {
        when(userService.getConnectedUser()).thenReturn(doctor);
        when(patientRepository.findAllByDoctorId(doctor.getId())).thenReturn(List.of(patient));
        when(dtoMapper.patientToDTO(patient)).thenReturn(patientDTO);

        List<PatientDTO> list = patientService.getPatients();
        assertEquals(1, list.size());
        assertEquals(patientDTO, list.get(0));
    }

    @Test
    void getPatient_authorized() {
        when(userService.getConnectedUser()).thenReturn(doctor);
        when(patientRepository.findById("p1")).thenReturn(Optional.of(patient));
        when(dtoMapper.patientToDTO(patient)).thenReturn(patientDTO);

        PatientDTO result = patientService.getPatient("p1");
        assertEquals(patientDTO, result);
    }

    @Test
    void getPatient_unauthorized_throwsException() {
        User otherDoctor = new User();
        otherDoctor.setId("d2");
        patient.setDoctor(otherDoctor);

        when(userService.getConnectedUser()).thenReturn(doctor);
        when(patientRepository.findById("p1")).thenReturn(Optional.of(patient));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> patientService.getPatient("p1"));
        assertEquals(403, ex.getStatusCode().value());
    }

    @Test
    void addPatient_success() {
        when(userService.getConnectedUser()).thenReturn(doctor);
        when(userService.getUser(doctor.getId())).thenReturn(doctor);
        doNothing().when(dtoValidation).checkPatientCreationEmailIsValid(patientDTO, bindingResult);
        doNothing().when(dtoValidation).validateBindingResult(bindingResult);
        when(dtoMapper.dtoToPatient(any(), eq(patientDTO), eq(doctor))).thenReturn(patient);
        when(dtoMapper.patientToDTO(patient)).thenReturn(patientDTO);

        PatientDTO result = patientService.addPatient(patientDTO, bindingResult);
        assertEquals(patientDTO, result);
        verify(patientRepository).save(patient);
    }

    @Test
    void addPatient_unauthorized_throwsException() {
        User otherDoctor = new User();
        otherDoctor.setId("d2");

        when(userService.getConnectedUser()).thenReturn(doctor);
        when(userService.getUser(otherDoctor.getId())).thenReturn(otherDoctor);
        doNothing().when(dtoValidation).checkPatientCreationEmailIsValid(patientDTO, bindingResult);
        doNothing().when(dtoValidation).validateBindingResult(bindingResult);
        patientDTO.setDoctorId(otherDoctor.getId());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> patientService.addPatient(patientDTO, bindingResult));
        assertEquals(403, ex.getStatusCode().value());
    }

    @Test
    void updatePatient_success() {
        when(userService.getConnectedUser()).thenReturn(doctor);
        when(patientRepository.findById("p1")).thenReturn(Optional.of(patient));
        doNothing().when(dtoValidation).checkPatientUpdateEmailIsValid(patientDTO, patient, bindingResult);
        doNothing().when(dtoValidation).validateBindingResult(bindingResult);
        when(dtoMapper.dtoToPatient(patient, patientDTO, doctor)).thenReturn(patient);
        when(dtoMapper.patientToDTO(patient)).thenReturn(patientDTO);

        PatientDTO result = patientService.updatePatient("p1", patientDTO, bindingResult);
        assertEquals(patientDTO, result);
        verify(patientRepository).save(patient);
    }

    @Test
    void deletePatient_success() {
        when(userService.getConnectedUser()).thenReturn(doctor);
        when(patientRepository.findById("p1")).thenReturn(Optional.of(patient));
        doNothing().when(patientRepository).delete(patient);

        String result = patientService.deletePatient("p1");
        assertEquals("Le patient a été supprimé avec succès", result);
        verify(patientRepository).delete(patient);
    }

    @Test
    void deletePatient_unauthorized_throwsException() {
        User otherDoctor = new User();
        otherDoctor.setId("d2");
        patient.setDoctor(otherDoctor);

        when(userService.getConnectedUser()).thenReturn(doctor);
        when(patientRepository.findById("p1")).thenReturn(Optional.of(patient));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> patientService.deletePatient("p1"));
        assertEquals(403, ex.getStatusCode().value());
    }
}
