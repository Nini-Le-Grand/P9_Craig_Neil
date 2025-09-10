package com.medilabo.user_ms.unit;

import com.medilabo.user_ms.domain.dto.PasswordDTO;
import com.medilabo.user_ms.domain.dto.UserDTO;
import com.medilabo.user_ms.domain.entity.Patient;
import com.medilabo.user_ms.domain.entity.User;
import com.medilabo.user_ms.domain.enums.Role;
import com.medilabo.user_ms.repository.PatientRepository;
import com.medilabo.user_ms.repository.UserRepository;
import com.medilabo.user_ms.service.UserService;
import com.medilabo.user_ms.utils.CreatePassword;
import com.medilabo.user_ms.utils.DTOMapper;
import com.medilabo.user_ms.utils.DTOValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DTOMapper dtoMapper;

    @Mock
    private DTOValidation dtoValidation;

    @Mock
    private CreatePassword createPassword;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;
    private BindingResult bindingResult;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId("u1");
        user.setEmail("user@example.com");
        user.setRole(Role.USER);

        userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());

        bindingResult = new BeanPropertyBindingResult(userDTO, "userDTO");
    }

    @Test
    void getConnectedUser_success() {
        // Mock SecurityContext ici uniquement pour ce test
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user.getEmail());

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User result = userService.getConnectedUser();
        assertEquals(user, result);
    }

    @Test
    void getConnectedUser_notFound_throwsException() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user.getEmail());

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> userService.getConnectedUser());
        assertEquals(401, ex.getStatusCode().value());
    }

    @Test
    void getUser_success() {
        when(userRepository.findById("u1")).thenReturn(Optional.of(user));

        User result = userService.getUser("u1");
        assertEquals(user, result);
    }

    @Test
    void getUser_notFound_throwsException() {
        when(userRepository.findById("u1")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> userService.getUser("u1"));
        assertEquals(404, ex.getStatusCode().value());
    }

    @Test
    void createUser_success() {
        when(createPassword.createDefaultPassword(user)).thenReturn("defaultPass");
        when(passwordEncoder.encode("defaultPass")).thenReturn("encodedPass");
        when(dtoMapper.dtoToUser(any(), eq(userDTO))).thenReturn(user);
        when(dtoMapper.userToDTO(user)).thenReturn(userDTO);
        doNothing().when(dtoValidation).validateBindingResult(bindingResult);
        doNothing().when(dtoValidation).checkUserCreationEmailIsValid(userDTO, bindingResult);

        UserDTO result = userService.createUser(userDTO, bindingResult);

        assertEquals(userDTO, result);
        assertEquals("encodedPass", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void updateUserPassword_success() {
        // Mock SecurityContext uniquement pour ce test
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user.getEmail());

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setNewPassword("newPass");
        passwordDTO.setConfirmPassword("newPass");

        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");
        doNothing().when(dtoValidation).validateBindingResult(bindingResult);
        doNothing().when(dtoValidation).checkPasswordAuth(passwordDTO, user, bindingResult);
        doNothing().when(dtoValidation).checkPasswordFormat(passwordDTO, bindingResult);
        doNothing().when(dtoValidation).checkConfirmPassword(passwordDTO, bindingResult);
        when(dtoMapper.userToDTO(user)).thenReturn(userDTO);

        String result = userService.updateUserPassword(passwordDTO, bindingResult);

        assertEquals("Mot de passe mis à jour avec succès", result);
        assertEquals("encodedNewPass", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void resetUserPassword_success() {
        when(createPassword.createDefaultPassword(user)).thenReturn("defaultPass");
        when(passwordEncoder.encode("defaultPass")).thenReturn("encodedPass");
        when(dtoMapper.userToDTO(user)).thenReturn(userDTO);

        String result = userService.resetUserPassword(user);

        assertTrue(result.contains("defaultPass"));
        assertEquals("encodedPass", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void deleteUser_success() {
        when(patientRepository.findAllByDoctorId(user.getId())).thenReturn(List.of());
        user.setRole(Role.USER);

        String result = userService.deleteUser(user);
        assertEquals("Utilisateur supprimé avec succès", result);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_hasPatients_throwsException() {
        when(patientRepository.findAllByDoctorId(user.getId())).thenReturn(List.of(new Patient()));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> userService.deleteUser(user));
        assertEquals(400, ex.getStatusCode().value());
    }

    @Test
    void deleteUser_isAdmin_throwsException() {
        user.setRole(Role.ADMIN);
        when(patientRepository.findAllByDoctorId(user.getId())).thenReturn(List.of());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> userService.deleteUser(user));
        assertEquals(400, ex.getStatusCode().value());
    }
}
