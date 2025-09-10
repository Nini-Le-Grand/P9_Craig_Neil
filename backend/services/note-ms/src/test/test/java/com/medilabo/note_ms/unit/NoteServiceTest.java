package com.medilabo.note_ms.service;

import com.medilabo.note_ms.client.PatientClient;
import com.medilabo.note_ms.domain.dto.NoteDTO;
import com.medilabo.note_ms.domain.dto.PatientDTO;
import com.medilabo.note_ms.domain.entity.Note;
import com.medilabo.note_ms.exception.ValidationException;
import com.medilabo.note_ms.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class NoteServiceTest {

    @InjectMocks
    private NoteService noteService;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private PatientClient patientClient;

    @Mock
    private BindingResult bindingResult;

    private Note note;
    private NoteDTO noteDTO;
    private PatientDTO patientDTO;

    @BeforeEach
    void setUp() {
        openMocks(this);

        note = new Note();
        note.setId("note-1");
        note.setPatientId("patient-123");
        note.setNote("Ceci est une note médicale");
        note.setDateTime(LocalDateTime.now());

        noteDTO = new NoteDTO();
        noteDTO.setPatientId("patient-123");
        noteDTO.setNote("Ceci est une note médicale");

        patientDTO = new PatientDTO();
        patientDTO.setId("patient-123");
        patientDTO.setLastName("Doe");
    }

    @Test
    void testGetNotesByPatientId() {
        given(noteRepository.findByPatientId("patient-123")).willReturn(List.of(note));

        List<Note> notes = noteService.getNotesByPatientId("patient-123");

        assertEquals(1, notes.size());
        assertEquals("note-1", notes.get(0).getId());
    }

    @Test
    void testCreateNote_Success() {
        given(bindingResult.hasErrors()).willReturn(false);
        given(patientClient.getPatientById("patient-123")).willReturn(patientDTO);
        given(noteRepository.save(any(Note.class))).willReturn(note);

        Note created = noteService.create(noteDTO, bindingResult);

        assertNotNull(created);
        assertEquals("patient-123", created.getPatientId());
        assertEquals("Ceci est une note médicale", created.getNote());
    }

    @Test
    void testCreateNote_WithValidationErrors() {
        given(bindingResult.hasErrors()).willReturn(true);
        given(bindingResult.getAllErrors()).willReturn(List.of(new ObjectError("noteDTO", "error")));

        assertThrows(ValidationException.class, () -> noteService.create(noteDTO, bindingResult));
    }

    @Test
    void testUpdateNote_Success() {
        given(bindingResult.hasErrors()).willReturn(false);
        given(noteRepository.findById("note-1")).willReturn(Optional.of(note));
        given(noteRepository.save(any(Note.class))).willReturn(note);

        Note updated = noteService.update(noteDTO, "note-1", bindingResult);

        assertEquals("Ceci est une note médicale", updated.getNote());
    }

    @Test
    void testUpdateNote_NotFound() {
        given(noteRepository.findById("note-1")).willReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> noteService.update(noteDTO, "note-1", bindingResult));

        assertEquals("La note n'existe pas", ex.getReason());
    }

    @Test
    void testUpdateNote_WithValidationErrors() {
        given(bindingResult.hasErrors()).willReturn(true);
        given(bindingResult.getAllErrors()).willReturn(List.of(new ObjectError("noteDTO", "error")));

        assertThrows(ValidationException.class, () -> noteService.update(noteDTO, "note-1", bindingResult));
    }

    @Test
    void testDeleteNote_Success() {
        given(noteRepository.findById("note-1")).willReturn(Optional.of(note));
        willDoNothing().given(noteRepository).delete(note);

        String message = noteService.delete("note-1");

        assertEquals("Note supprimée avec succès", message);
        then(noteRepository).should().delete(note);
    }

    @Test
    void testDeleteNote_NotFound() {
        given(noteRepository.findById("note-1")).willReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                                                  () -> noteService.delete("note-1"));

        assertEquals("La note n'existe pas", ex.getReason());
    }
}
