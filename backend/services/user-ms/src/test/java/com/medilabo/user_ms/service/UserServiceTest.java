/*
package com.medilabo.users.service;

import com.medilabo.users.dto.PasswordDTO;
import com.medilabo.users.dto.PatientDTO;
import com.medilabo.users.dto.UserDTO;
import com.medilabo.users.domain.enums.Role;
import com.medilabo.users.entity.User;
import com.medilabo.users.repository.UserRepository;
import com.medilabo.users.utils.DTOMapper;
import com.medilabo.users.utils.DTOValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserRepository userRepository;
    @Mock private DTOMapper dtoMapper;
    @Mock private DTOValidation dtoValidation;
    @InjectMocks private UserService userService;
    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@mail.com");
        user.setRole(Role.PATIENT);
        user.setPassword("encodedPassword");

        userDTO = new UserDTO();
        userDTO.setEmail("test@mail.com");
    }

    @Test
    void getConnectedUser_shouldReturnUser() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user.getEmail());

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        User result = userService.getConnectedUser();
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void getConnectedUser_shouldThrowIfNotFound() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user.getEmail());

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> userService.getConnectedUser());
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    }

    @Test
    void getUser_shouldReturnUser() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        User result = userService.getUser(user.getEmail());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void getUser_shouldThrowIfNotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> userService.getUser(user.getEmail()));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void getUserDTO_shouldReturnUserDTO() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user.getEmail());

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(dtoMapper.userToDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserDTO();
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void updateUserProfile_shouldReturnUpdatedUserDTO() {
        PatientDTO profileDTO = new PatientDTO();
        BindingResult result = mock(BindingResult.class);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        doNothing().when(dtoValidation).validateProfileDto(user, profileDTO, result);
        when(dtoMapper.profileDtoToUser(user, profileDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(dtoMapper.userToDTO(user)).thenReturn(userDTO);

        UserDTO updatedDTO = userService.updateUserProfile(profileDTO, result);
        assertEquals(user.getEmail(), updatedDTO.getEmail());
    }

    @Test
    void updateUserPassword_shouldReturnSuccessMessage() {
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setCurrentPassword("current");
        passwordDTO.setNewPassword("New123!");
        passwordDTO.setConfirmPassword("New123!");

        BindingResult result = mock(BindingResult.class);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        doNothing().when(dtoValidation).validatePasswordDto(user, passwordDTO, result);
        when(passwordEncoder.encode(passwordDTO.getNewPassword())).thenReturn("encodedNewPass");
        when(userRepository.save(user)).thenReturn(user);

        String message = userService.updateUserPassword(passwordDTO, result);
        assertEquals("Mot de passe mis à jour avec succès", message);
        assertEquals("encodedNewPass", user.getPassword());
    }

    @Test
    void saveUser_shouldReturnUserDTO() {
        when(userRepository.save(user)).thenReturn(user);
        when(dtoMapper.userToDTO(user)).thenReturn(userDTO);

        UserDTO saved = userService.saveUser(user);
        assertEquals(user.getEmail(), saved.getEmail());
    }

    @Test
    void saveUser_shouldThrowOnException() {
        when(userRepository.save(user)).thenThrow(new RuntimeException("fail"));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> userService.saveUser(user));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ex.getStatusCode());
    }
}
*/
