package com.medilabo.user_ms.unit;

import com.medilabo.user_ms.domain.dto.PasswordDTO;
import com.medilabo.user_ms.domain.dto.PatientDTO;
import com.medilabo.user_ms.domain.dto.UserDTO;
import com.medilabo.user_ms.domain.entity.Patient;
import com.medilabo.user_ms.domain.entity.User;
import com.medilabo.user_ms.domain.enums.Role;
import com.medilabo.user_ms.exception.ValidationException;
import com.medilabo.user_ms.repository.PatientRepository;
import com.medilabo.user_ms.repository.UserRepository;
import com.medilabo.user_ms.utils.DTOValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DTOValidationUnitTest {

    @InjectMocks
    private DTOValidation dtoValidation;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testValidateBindingResult_throwsException() {
        UserDTO dto = new UserDTO();
        BindingResult result = new BeanPropertyBindingResult(dto, "userDTO");
        result.rejectValue("email", "400", "Email invalide");

        assertThrows(ValidationException.class,
                     () -> dtoValidation.validateBindingResult(result));
    }

    @Test
    void testCheckUserCreationEmailIsValid_alreadyUsed() {
        UserDTO dto = new UserDTO();
        dto.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(new User()));

        BindingResult result = new BeanPropertyBindingResult(dto, "userDTO");
        dtoValidation.checkUserCreationEmailIsValid(dto, result);

        assertTrue(result.hasErrors());
        assertEquals("Cet email est déjà utilisé",
                     result.getFieldError("email").getDefaultMessage());
    }

    @Test
    void testCheckUserUpdateEmailIsValid_conflict() {
        UserDTO dto = new UserDTO();
        dto.setEmail("test2@example.com");

        User existingUser = new User();
        existingUser.setEmail("test2@example.com");

        when(userRepository.findByEmail("test2@example.com"))
                .thenReturn(Optional.of(existingUser));

        BindingResult result = new BeanPropertyBindingResult(dto, "userDTO");
        dtoValidation.checkUserUpdateEmailIsValid(dto, "other@example.com", result);

        assertTrue(result.hasErrors());
        assertEquals("Cet email est déjà utilisé",
                     result.getFieldError("email").getDefaultMessage());
    }

    @Test
    void testCheckPatientCreationEmailIsValid_alreadyUsed() {
        PatientDTO dto = new PatientDTO();
        dto.setEmail("patient@example.com");

        when(patientRepository.findByEmail("patient@example.com"))
                .thenReturn(Optional.of(new Patient()));

        BindingResult result = new BeanPropertyBindingResult(dto, "patientDTO");
        dtoValidation.checkPatientCreationEmailIsValid(dto, result);

        assertTrue(result.hasErrors());
        assertEquals("Cet email est déjà utilisé",
                     result.getFieldError("email").getDefaultMessage());
    }

    @Test
    void testCheckPatientUpdateEmailIsValid_conflict() {
        PatientDTO dto = new PatientDTO();
        dto.setEmail("patient2@example.com");

        Patient patient = new Patient();
        patient.setEmail("patient@example.com");

        Patient existingPatient = new Patient();
        existingPatient.setEmail("patient2@example.com");

        when(patientRepository.findByEmail("patient2@example.com"))
                .thenReturn(Optional.of(existingPatient));

        BindingResult result = new BeanPropertyBindingResult(dto, "patientDTO");
        dtoValidation.checkPatientUpdateEmailIsValid(dto, patient, result);

        assertTrue(result.hasErrors());
        assertEquals("Cet email est déjà utilisé",
                     result.getFieldError("email").getDefaultMessage());
    }

    @Test
    void testCheckDoctorExists_notFound() {
        PatientDTO dto = new PatientDTO();
        dto.setDoctorId("doc123");

        when(userRepository.findById("doc123")).thenReturn(Optional.empty());

        BindingResult result = new BeanPropertyBindingResult(dto, "patientDTO");
        dtoValidation.checkDoctorExists(dto, result);

        assertTrue(result.hasErrors());
        assertEquals("Le docteur n'existe pas",
                     result.getFieldError("doctorId").getDefaultMessage());
    }

    @Test
    void testCheckDoctorExists_wrongRole() {
        PatientDTO dto = new PatientDTO();
        dto.setDoctorId("doc123");

        User doctor = new User();
        doctor.setRole(Role.ADMIN);

        when(userRepository.findById("doc123")).thenReturn(Optional.of(doctor));

        BindingResult result = new BeanPropertyBindingResult(dto, "patientDTO");
        dtoValidation.checkDoctorExists(dto, result);

        assertTrue(result.hasErrors());
        assertEquals("Le docteur n'existe pas",
                     result.getFieldError("doctorId").getDefaultMessage());
    }

    @Test
    void testCheckPasswordAuth_incorrectPassword() {
        User user = new User();
        user.setId("u1");
        user.setPassword("encodedPassword");

        PasswordDTO dto = new PasswordDTO();
        dto.setCurrentPassword("wrongPassword");

        when(passwordEncoder.matches("wrongPassword", "encodedPassword"))
                .thenReturn(false);

        BindingResult result = new BeanPropertyBindingResult(dto, "passwordDTO");
        dtoValidation.checkPasswordAuth(dto, user, result);

        assertTrue(result.hasErrors());
        assertEquals("Mot de passe incorrecte",
                     result.getFieldError("currentPassword").getDefaultMessage());
    }

    @Test
    void testCheckPasswordAuth_correctPassword() {
        User user = new User();
        user.setId("u1");
        user.setPassword("encodedPassword");

        PasswordDTO dto = new PasswordDTO();
        dto.setCurrentPassword("Password123!");

        when(passwordEncoder.matches("Password123!", "encodedPassword"))
                .thenReturn(true);

        BindingResult result = new BeanPropertyBindingResult(dto, "PasswordDTO");
        dtoValidation.checkPasswordAuth(dto, user, result);

        assertFalse(result.hasErrors());
    }

    @Test
    void testCheckPasswordFormat_invalid() {
        PasswordDTO dto = new PasswordDTO();
        dto.setNewPassword("abc");

        BindingResult result = new BeanPropertyBindingResult(dto, "passwordDTO");
        dtoValidation.checkPasswordFormat(dto, result);

        assertTrue(result.hasErrors());
        assertEquals("Format du mot de passe invalide",
                     result.getFieldError("newPassword").getDefaultMessage());
    }

    @Test
    void testCheckPasswordFormat_valid() {
        PasswordDTO dto = new PasswordDTO();
        dto.setNewPassword("Abcdef1!");

        BindingResult result = new BeanPropertyBindingResult(dto, "passwordDTO");
        dtoValidation.checkPasswordFormat(dto, result);

        assertFalse(result.hasErrors());
    }

    @Test
    void testCheckConfirmPassword_mismatch() {
        PasswordDTO dto = new PasswordDTO();
        dto.setNewPassword("Abcdef1!");
        dto.setConfirmPassword("Mismatch1!");

        BindingResult result = new BeanPropertyBindingResult(dto, "passwordDTO");
        dtoValidation.checkConfirmPassword(dto, result);

        assertTrue(result.hasErrors());
        assertEquals("Les mots de passe sont différents",
                     result.getFieldError("confirmPassword").getDefaultMessage());
    }

    @Test
    void testCheckConfirmPassword_match() {
        PasswordDTO dto = new PasswordDTO();
        dto.setNewPassword("Abcdef1!");
        dto.setConfirmPassword("Abcdef1!");

        BindingResult result = new BeanPropertyBindingResult(dto, "passwordDTO");
        dtoValidation.checkConfirmPassword(dto, result);

        assertFalse(result.hasErrors());
    }
}
