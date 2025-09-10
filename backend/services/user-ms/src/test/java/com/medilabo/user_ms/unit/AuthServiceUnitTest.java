package com.medilabo.user_ms.unit;

import com.medilabo.user_ms.domain.dto.LoginDTO;
import com.medilabo.user_ms.domain.entity.User;
import com.medilabo.user_ms.repository.UserRepository;
import com.medilabo.user_ms.security.JwtUtils;
import com.medilabo.user_ms.service.AuthService;
import com.medilabo.user_ms.utils.DTOValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private DTOValidation dtoValidation;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private LoginDTO loginDTO;
    private BindingResult bindingResult;

    @BeforeEach
    void setup() {
        loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("Password123!");
        bindingResult = new BeanPropertyBindingResult(loginDTO, "loginDTO");
    }

    @Test
    void login_userNotFound_throwsUnauthorized() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> authService.login(loginDTO, bindingResult));
        assertEquals(401, ex.getStatusCode().value());
        assertTrue(ex.getReason().contains("Identifiant ou mot de passe"));
    }

    @Test
    void login_incorrectPassword_throwsUnauthorized() {
        User user = new User();
        user.setPassword("encodedPassword");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password123!", "encodedPassword")).thenReturn(false);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> authService.login(loginDTO, bindingResult));
        assertEquals(401, ex.getStatusCode().value());
    }

    @Test
    void login_success_returnsToken() {
        User user = new User();
        user.setId("u1");
        user.setPassword("encodedPassword");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password123!", "encodedPassword")).thenReturn(true);
        when(jwtUtils.generateToken(user)).thenReturn("jwt-token");

        String token = authService.login(loginDTO, bindingResult);
        assertEquals("jwt-token", token);
    }

    @Test
    void login_bindingResultHasErrors_throwsValidationException() {
        bindingResult.rejectValue("email", "400", "Email invalide");
        doThrow(new RuntimeException("Validation failed")).when(dtoValidation).validateBindingResult(bindingResult);

        RuntimeException ex = assertThrows(RuntimeException.class,
                                           () -> authService.login(loginDTO, bindingResult));
        assertEquals("Validation failed", ex.getMessage());
    }
}
