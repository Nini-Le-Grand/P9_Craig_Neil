/*
package com.medilabo.users.utils;

import com.medilabo.users.dto.PasswordDTO;
import com.medilabo.users.dto.PatientDTO;
import com.medilabo.users.entity.User;
import com.medilabo.users.exception.ValidationException;
import com.medilabo.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DTOValidationTest {
    @InjectMocks
    private DTOValidation dtoValidation;
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private BindingResult bindingResult;

    // ───── validateLoginDto ─────

    @Test
    void validateLoginDto_shouldThrow_whenHasErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(ValidationException.class, () -> dtoValidation.validateLoginDto(bindingResult));
        verify(bindingResult).hasErrors();
    }

    @Test
    void validateLoginDto_shouldPass_whenNoErrors() {
        when(bindingResult.hasErrors()).thenReturn(false);

        assertDoesNotThrow(() -> dtoValidation.validateLoginDto(bindingResult));
    }

    @Test
    void validateProfileDto_shouldRejectEmail_whenCreatingUserAndEmailExists() {
        PatientDTO dto = new PatientDTO();
        dto.setEmail("used@mail.com");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new User()));

        dtoValidation.validateProfileDto(null, dto, bindingResult);

        verify(bindingResult).rejectValue(eq("email"), eq("400"), anyString());
    }

    @Test
    void validateProfileDto_shouldRejectEmail_whenUpdatingUserWithConflictingEmail() {
        PatientDTO dto = new PatientDTO();
        dto.setEmail("other@mail.com");

        User current = new User();
        current.setEmail("original@mail.com");

        User existing = new User();
        existing.setEmail("other@mail.com");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(existing));

        dtoValidation.validateProfileDto(current, dto, bindingResult);

        verify(bindingResult).rejectValue(eq("email"), eq("400"), anyString());
    }

    @Test
    void validateProfileDto_shouldThrow_whenBindingResultHasErrors() {
        PatientDTO dto = new PatientDTO();
        dto.setEmail("mail@mail.com");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(ValidationException.class, () -> dtoValidation.validateProfileDto(new User(), dto, bindingResult));
    }

    @Test
    void validateProfileDto_shouldPass_whenValid() {
        PatientDTO dto = new PatientDTO();
        dto.setEmail("mail@mail.com");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(bindingResult.hasErrors()).thenReturn(false);

        assertDoesNotThrow(() -> dtoValidation.validateProfileDto(new User(), dto, bindingResult));
    }

    @Test
    void validatePasswordDto_shouldReject_ifCurrentPasswordIsIncorrect() {
        User user = new User();
        user.setPassword("encoded");

        PasswordDTO dto = new PasswordDTO();
        dto.setCurrentPassword("wrong");
        dto.setNewPassword("short");
        dto.setConfirmPassword("short");

        when(passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())).thenReturn(false);

        dtoValidation.validatePasswordDto(user, dto, bindingResult);

        verify(bindingResult).rejectValue(eq("currentPassword"), eq("400"), anyString());
    }

    @Test
    void validatePasswordDto_shouldReject_ifNewPasswordInvalidFormat() {
        User user = new User();
        user.setPassword("encoded");

        PasswordDTO dto = new PasswordDTO();
        dto.setCurrentPassword("Valid123!");
        dto.setNewPassword("short");
        dto.setConfirmPassword("short");

        when(passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())).thenReturn(true);

        dtoValidation.validatePasswordDto(user, dto, bindingResult);

        verify(bindingResult).rejectValue(eq("newPassword"), eq("400"), anyString());
    }

    @Test
    void validatePasswordDto_shouldReject_ifPasswordsDontMatch() {
        User user = new User();
        user.setPassword("encoded");

        PasswordDTO dto = new PasswordDTO();
        dto.setCurrentPassword("Valid123!");
        dto.setNewPassword("Match123!");
        dto.setConfirmPassword("Mismatch123!");

        when(passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())).thenReturn(true);

        dtoValidation.validatePasswordDto(user, dto, bindingResult);

        verify(bindingResult).rejectValue(eq("confirmPassword"), eq("400"), anyString());
    }

    @Test
    void validatePasswordDto_shouldThrow_whenErrorsExist() {
        User user = new User();
        user.setPassword("encoded");

        PasswordDTO dto = new PasswordDTO();
        dto.setCurrentPassword("Valid123!");
        dto.setNewPassword("Match123!");
        dto.setConfirmPassword("Match123!");

        when(passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())).thenReturn(true);
        when(bindingResult.hasErrors()).thenReturn(true);

        assertThrows(ValidationException.class, () -> dtoValidation.validatePasswordDto(user, dto, bindingResult));
    }

    @Test
    void validatePasswordDto_shouldPass_whenValid() {
        User user = new User();
        user.setPassword("encoded");

        PasswordDTO dto = new PasswordDTO();
        dto.setCurrentPassword("Valid123!");
        dto.setNewPassword("Match123!");
        dto.setConfirmPassword("Match123!");

        when(passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())).thenReturn(true);
        when(bindingResult.hasErrors()).thenReturn(false);

        assertDoesNotThrow(() -> dtoValidation.validatePasswordDto(user, dto, bindingResult));
    }
}
*/
