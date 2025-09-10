package com.medilabo.note_ms.integration;

import com.medilabo.note_ms.domain.entity.Note;
import com.medilabo.note_ms.repository.NoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
public class NoteIntegrationTest {
    @Autowired
    private NoteRepository noteRepository;

    @Test
    void shouldSaveAndRetrieveNote() {
        // Création d'une note
        Note note = Note.builder()
                        .patientId("patient-123")
                        .dateTime(LocalDateTime.now())
                        .note("Ceci est une note médicale")
                        .build();

        // Sauvegarde dans MongoDB
        Note savedNote = noteRepository.save(note);

        assertThat(savedNote.getId()).isNotNull();

        // Récupération depuis MongoDB
        Optional<Note> retrievedNote = noteRepository.findById(savedNote.getId());
        assertThat(retrievedNote).isPresent();
        assertThat(retrievedNote.get().getPatientId()).isEqualTo("patient-123");
        assertThat(retrievedNote.get().getNote()).isEqualTo("Ceci est une note médicale");
    }
}
