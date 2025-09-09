package com.medilabo.note_ms.domain.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for a patient.
 * Used to transfer minimal patient information between services or API layers.
 */
@Getter
@Setter
public class PatientDTO {

    /** Unique identifier of the patient. */
    private String id;

    /** Last name of the patient. */
    private String lastName;
}
