package com.medilabo.note_ms.domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Represents a medical note associated with a patient.
 * Stored in the "notes" MongoDB collection.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "notes")
public class Note {

    /** Unique identifier of the note. */
    @Id
    private String id;

    /** Identifier of the patient this note belongs to. */
    private String patientId;

    /** Date and time when the note was created. */
    private LocalDateTime dateTime;

    /** Content of the note. */
    private String note;
}
