package com.medilabo.user_ms.domain.dto;

import com.medilabo.user_ms.domain.enums.Gender;
import com.medilabo.user_ms.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * Data Transfer Object for User entity.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    /**
     * Unique identifier of the user.
     */
    private String id;

    /**
     * User's first name.
     * Must not be blank.
     */
    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    /**
     * User's last name.
     * Must not be blank.
     */
    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;

    /**
     * User's date of birth.
     * Must not be null.
     */
    @NotNull(message = "La date de naissance est obligatoire")
    private LocalDate dateOfBirth;

    /**
     * User's gender.
     * Must not be null.
     */
    @NotNull(message = "Le genre est obligatoire")
    private Gender gender;

    /**
     * User's email address.
     * Must not be blank and must be a valid email format.
     */
    @NotBlank(message = "L'email est obligatoire")
    @Email
    private String email;

    /**
     * User's address.
     */
    private String address;

    /**
     * User's phone number.
     */
    private String phone;

    /**
     * User's role (e.g., USER or ADMIN).
     */
    private Role role;
}
