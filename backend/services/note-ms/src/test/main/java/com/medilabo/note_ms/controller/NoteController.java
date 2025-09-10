package com.medilabo.note_ms.controller;

import com.medilabo.note_ms.domain.dto.NoteDTO;
import com.medilabo.note_ms.domain.entity.Note;
import com.medilabo.note_ms.repository.NoteRepository;
import com.medilabo.note_ms.service.NoteService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing medical notes.
 * Provides endpoints for CRUD operations on notes.
 */
@RestController
public class NoteController {

    private static final Logger logger = LogManager.getLogger(NoteController.class);

    @Autowired
    private NoteService noteService;

    /**
     * Retrieves all notes for a given patient.
     *
     * @param patientId the id of the patient
     * @return a list of notes
     */
    @GetMapping("/{patientId}")
    public ResponseEntity<List<Note>> getNotesByPatientId(@PathVariable String patientId) {
        logger.info("GET /notes/{} - Retrieving notes for patient", patientId);
        List<Note> notes = noteService.getNotesByPatientId(patientId);
        logger.info("GET /notes/{} - Retrieved {} notes", patientId, notes.size());
        return ResponseEntity.ok(notes);
    }

    /**
     * Creates a new note for a patient.
     *
     * @param noteDTO the note data
     * @param result  binding result for validation
     * @return the created note
     */
    @PostMapping
    public ResponseEntity<Note> create(@Valid @RequestBody NoteDTO noteDTO, BindingResult result) {
        logger.info("POST /notes - Creating note for patient {}", noteDTO.getPatientId());
        Note note = noteService.create(noteDTO, result);
        logger.info("POST /notes - Note created with id {}", note.getId());
        return ResponseEntity.ok(note);
    }

    /**
     * Updates an existing note.
     *
     * @param id      the id of the note to update
     * @param noteDTO the updated note data
     * @param result  binding result for validation
     * @return the updated note
     */
    @PutMapping("/{id}")
    public ResponseEntity<Note> update(@PathVariable String id, @Valid @RequestBody NoteDTO noteDTO, BindingResult result) {
        logger.info("PUT /notes/{} - Updating note", id);
        Note note = noteService.update(noteDTO, id, result);
        logger.info("PUT /notes/{} - Note updated successfully", id);
        return ResponseEntity.ok(note);
    }

    /**
     * Deletes a note by its id.
     *
     * @param id the id of the note to delete
     * @return success message in French
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        logger.info("DELETE /notes/{} - Deleting note", id);
        String deleteMessage = noteService.delete(id);
        logger.info("DELETE /notes/{} - {}", id, deleteMessage);
        return ResponseEntity.ok(deleteMessage);
    }
}
