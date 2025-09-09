package com.medilabo.evaluation_ms.domain.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing a medical note.
 * <p>
 * A note is associated with a patient and contains free text
 * and the date/time when the note was created.
 */
@Data
public class NoteDTO {

    /** Unique identifier of the note */
    private String id;

    /** Content of the medical note */
    private String note;

    /** Date and time when the note was created */
    private LocalDateTime dateTime;
}
