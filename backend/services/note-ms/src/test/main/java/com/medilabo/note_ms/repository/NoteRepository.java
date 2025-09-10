package com.medilabo.note_ms.repository;

import com.medilabo.note_ms.domain.entity.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Note entities in MongoDB.
 * Provides basic CRUD operations and custom query methods.
 */
@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    /**
     * Retrieves all notes associated with a specific patient ID.
     *
     * @param patientId ID of the patient
     * @return list of notes for the given patient
     */
    List<Note> findByPatientId(String patientId);
}
