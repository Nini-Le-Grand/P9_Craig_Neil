package com.medilabo.evaluation_ms.domain.dto;

import com.medilabo.evaluation_ms.domain.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a patient.
 * <p>
 * Contains basic patient information needed for evaluation,
 * such as gender and date of birth.
 */
@Data
public class PatientDTO {

    /** Unique identifier of the patient */
    private String id;

    /** Gender of the patient (e.g., M, F) */
    private Gender gender;

    /** Date of birth of the patient */
    private LocalDate dateOfBirth;
}
