package com.medilabo.note_ms.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for a medical note.
 * Used to transfer note data between client, service, and database layers.
 */
@Getter
@Setter
public class NoteDTO {

    /** Unique identifier of the note. */
    private String id;

    /** Identifier of the patient this note belongs to. */
    private String patientId;

    /** Content of the note. Cannot be blank. */
    @NotBlank(message = "La note ne peux pas être vide")
    private String note;
}
