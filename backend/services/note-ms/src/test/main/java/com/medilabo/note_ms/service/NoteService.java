package com.medilabo.note_ms.service;

import com.medilabo.note_ms.client.PatientClient;
import com.medilabo.note_ms.domain.dto.NoteDTO;
import com.medilabo.note_ms.domain.dto.PatientDTO;
import com.medilabo.note_ms.domain.entity.Note;
import com.medilabo.note_ms.exception.ValidationException;
import com.medilabo.note_ms.repository.NoteRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service responsible for managing notes.
 */
@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private PatientClient patientClient;

    private static final Logger logger = LogManager.getLogger(NoteService.class);

    /**
     * Retrieves a note by its ID.
     *
     * @param id note ID
     * @return Note entity
     * @throws ResponseStatusException if the note does not exist
     */
    private Note getNote(String id) {
        logger.info("Fetching note by ID: {}", id);
        return noteRepository.findById(id).orElseThrow(() -> {
            String errorMessage = "La note n'existe pas";
            logger.error(errorMessage);
            return new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
        });
    }

    /**
     * Retrieves all notes for a given patient.
     *
     * @param patientId patient ID
     * @return list of Note entities
     */
    public List<Note> getNotesByPatientId(String patientId) {
        logger.info("Fetching notes for patient ID: {}", patientId);
        List<Note> notes = noteRepository.findByPatientId(patientId);
        logger.debug("Number of notes found: {}", notes.size());
        return notes;
    }

    /**
     * Creates a new note for a patient.
     *
     * @param noteDTO note data
     * @param result  binding result for validation
     * @return created Note entity
     */
    public Note create(NoteDTO noteDTO, BindingResult result) {
        if (result.hasErrors()) {
            logger.error("Validation errors when creating note: {}", result.getAllErrors());
            throw new ValidationException(result);
        }

        logger.info("Creating note for patient ID: {}", noteDTO.getPatientId());
        PatientDTO patient = patientClient.getPatientById(noteDTO.getPatientId());

        Note note = new Note();
        note.setPatientId(patient.getId());
        note.setNote(noteDTO.getNote());
        note.setDateTime(LocalDateTime.now());

        Note savedNote = noteRepository.save(note);
        logger.debug("Note created with ID: {}", savedNote.getId());
        return savedNote;
    }

    /**
     * Updates an existing note.
     *
     * @param noteDTO note data
     * @param id      note ID
     * @param result  binding result for validation
     * @return updated Note entity
     */
    public Note update(NoteDTO noteDTO, String id, BindingResult result) {
        if (result.hasErrors()) {
            logger.error("Validation errors when updating note: {}", result.getAllErrors());
            throw new ValidationException(result);
        }

        logger.info("Updating note ID: {}", id);
        Note note = getNote(id);
        note.setNote(noteDTO.getNote());

        Note updatedNote = noteRepository.save(note);
        logger.debug("Note updated with ID: {}", updatedNote.getId());
        return updatedNote;
    }

    /**
     * Deletes a note by ID.
     *
     * @param id note ID
     * @return success message
     */
    public String delete(String id) {
        logger.info("Deleting note ID: {}", id);
        Note note = getNote(id);
        noteRepository.delete(note);
        logger.debug("Note deleted successfully: ID {}", id);

        return "Note supprimée avec succès";
    }
}
