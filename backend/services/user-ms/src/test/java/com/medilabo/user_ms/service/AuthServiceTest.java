/*
package com.medilabo.users.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.medilabo.users.dto.LoginDTO;
import com.medilabo.users.entity.User;
import com.medilabo.users.repository.UserRepository;
import com.medilabo.users.utils.DTOValidation;
import com.medilabo.users.security.JwtUtils;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthServiceTest {
    @Mock private UserRepository userRepository;
    @Mock private JwtUtils jwtUtils;
    @Mock private DTOValidation dtoValidation;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private BindingResult bindingResult;
    @InjectMocks private AuthService authService;
    private LoginDTO loginDTO;
    private User user;

    @BeforeEach
    void setup() {
        loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("password123");

        user = new User();
        user.setEmail(loginDTO.getEmail());
        user.setPassword("encodedPassword");
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        doNothing().when(dtoValidation).validateLoginDto(bindingResult);
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtils.generateToken(user)).thenReturn("jwtToken");

        String token = authService.login(loginDTO, bindingResult);

        assertEquals("jwtToken", token);
        verify(dtoValidation).validateLoginDto(bindingResult);
        verify(userRepository).findByEmail(loginDTO.getEmail());
        verify(passwordEncoder).matches(loginDTO.getPassword(), user.getPassword());
        verify(jwtUtils).generateToken(user);
    }

    @Test
    void login_shouldThrowUnauthorized_whenUserNotFound() {
        doNothing().when(dtoValidation).validateLoginDto(bindingResult);
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> authService.login(loginDTO, bindingResult));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Identifiant ou mot de passe incorrecte"));
    }

    @Test
    void login_shouldThrowUnauthorized_whenPasswordDoesNotMatch() {
        doNothing().when(dtoValidation).validateLoginDto(bindingResult);
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> authService.login(loginDTO, bindingResult));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Identifiant ou mot de passe incorrecte"));
    }

    @Test
    void login_shouldThrowInternalServerError_whenTokenGenerationFails() {
        doNothing().when(dtoValidation).validateLoginDto(bindingResult);
        when(userRepository.findByEmail(loginDTO.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtils.generateToken(user)).thenThrow(new JwtException("JWT error"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                                                         () -> authService.login(loginDTO, bindingResult));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Erreur interne lors de la connexion"));
    }
}
*/
