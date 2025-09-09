/*
package com.medilabo.users.utils;

import com.medilabo.users.dto.PatientDTO;
import com.medilabo.users.dto.UserDTO;
import com.medilabo.users.domain.enums.Gender;
import com.medilabo.users.domain.enums.Role;
import com.medilabo.users.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DTOMapperTest {
    private DTOMapper dtoMapper;

    @BeforeEach
    void setUp() {
        dtoMapper = new DTOMapper();
    }

    @Test
    void testUserToDTO_success() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        user.setPhone("0123456789");
        user.setGender(Gender.M);
        user.setEmail("john.doe@example.com");
        user.setAddress("123 rue Exemple");
        user.setRole(Role.PATIENT);

        User doctor = new User();
        doctor.setEmail("doctor@example.com");
        user.setDoctor(doctor);

        UserDTO dto = dtoMapper.userToDTO(user);

        assertAll("Mapped UserDTO fields",
                  () -> assertEquals("John", dto.getFirstName()),
                  () -> assertEquals("Doe", dto.getLastName()),
                  () -> assertEquals(LocalDate.of(1990, 1, 1), dto.getDateOfBirth()),
                  () -> assertEquals("0123456789", dto.getPhone()),
                  () -> assertEquals(Gender.M, dto.getGender()),
                  () -> assertEquals("john.doe@example.com", dto.getEmail()),
                  () -> assertEquals("123 rue Exemple", dto.getAddress()),
                  () -> assertEquals(Role.PATIENT, dto.getRole()),
                  () -> assertEquals("doctor@example.com", dto.getDoctor().getEmail())
        );
    }

    @Test
    void testUserToDTO_noDoctor() {
        User user = new User();
        user.setEmail("test@example.com");

        UserDTO dto = dtoMapper.userToDTO(user);

        assertAll("Mapped UserDTO with no doctor",
                  () -> assertEquals("test@example.com", dto.getEmail()),
                  () -> assertNull(dto.getDoctor())
        );
    }

    @Test
    void testDtoToUser_success() {
        PatientDTO profileDTO = new PatientDTO();
        profileDTO.setFirstName("Alice");
        profileDTO.setLastName("Smith");
        profileDTO.setDateOfBirth(LocalDate.of(1985, 5, 15));
        profileDTO.setPhone("0987654321");
        profileDTO.setGender(Gender.F);
        profileDTO.setEmail("alice.smith@example.com");
        profileDTO.setAddress("456 rue Exemple");

        User user = new User();
        user.setRole(Role.ADMIN);

        User updated = dtoMapper.profileDtoToUser(user, profileDTO);

        assertAll("Updated User fields",
                  () -> assertEquals("Alice", updated.getFirstName()),
                  () -> assertEquals("Smith", updated.getLastName()),
                  () -> assertEquals(LocalDate.of(1985, 5, 15), updated.getDateOfBirth()),
                  () -> assertEquals("0987654321", updated.getPhone()),
                  () -> assertEquals(Gender.F, updated.getGender()),
                  () -> assertEquals("alice.smith@example.com", updated.getEmail()),
                  () -> assertEquals("456 rue Exemple", updated.getAddress()),
                  () -> assertEquals(Role.ADMIN, updated.getRole()) // preserved
        );
    }
}
*/
