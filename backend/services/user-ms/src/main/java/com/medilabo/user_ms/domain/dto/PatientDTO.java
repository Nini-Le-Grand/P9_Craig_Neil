package com.medilabo.user_ms.domain.dto;

import com.medilabo.user_ms.domain.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * Data Transfer Object for Patient entity.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientDTO {

    /**
     * Unique identifier of the patient.
     */
    private String id;

    /**
     * Patient's first name.
     * Must not be blank.
     */
    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    /**
     * Patient's last name.
     * Must not be blank.
     */
    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;

    /**
     * Patient's date of birth.
     * Must not be null.
     */
    @NotNull(message = "La date de naissance est obligatoire")
    private LocalDate dateOfBirth;

    /**
     * Patient's gender.
     * Must not be null.
     */
    @NotNull(message = "Le genre est obligatoire")
    private Gender gender;

    /**
     * Patient's email.
     * Must not be blank and must be a valid email format.
     */
    @NotBlank(message = "L'email est obligatoire")
    @Email
    private String email;

    /**
     * Patient's address.
     */
    private String address;

    /**
     * Patient's phone number.
     */
    private String phone;

    /**
     * Identifier of the assigned doctor.
     * Must not be null.
     */
    @NotNull(message = "Le médecin est obligatoire")
    private String doctorId;
}
