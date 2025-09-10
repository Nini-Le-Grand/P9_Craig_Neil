package com.medilabo.note_ms.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.note_ms.client.PatientClient;
import com.medilabo.note_ms.domain.dto.NoteDTO;
import com.medilabo.note_ms.domain.dto.PatientDTO;
import com.medilabo.note_ms.domain.entity.Note;
import com.medilabo.note_ms.repository.NoteRepository;
import com.medilabo.note_ms.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
class NoteIntegrationTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private PatientClient patientClient;
    @Autowired private NoteRepository noteRepository;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PatientClient patientClient() {
            return Mockito.mock(PatientClient.class);
        }
    }

    private ObjectMapper objectMapper;
    private Note note;
    private NoteDTO noteDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        note = Note.builder()
                   .id("note-1")
                   .patientId("patient-123")
                   .note("Ceci est une note médicale")
                   .dateTime(LocalDateTime.now())
                   .build();

        noteRepository.save(note);

        noteDTO = new NoteDTO();
        noteDTO.setPatientId("patient-123");
        noteDTO.setNote("Ceci est une note médicale");
    }

    @Test
    void testCreateNote() throws Exception {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId("patient-123");

        when(patientClient.getPatientById("patient-123")).thenReturn(patientDTO);

        String token = jwtUtils.generateToken("USER", 36000L);

        mockMvc.perform(post("/")
                                .header("Authorization", "Bearer " + token)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(noteDTO)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.patientId").value(note.getPatientId()))
               .andExpect(jsonPath("$.note").value(note.getNote()));
    }

    @Test
    void testUpdateNote() throws Exception {
        String token = jwtUtils.generateToken("USER", 36000L);

        mockMvc.perform(put("/note-1")
                                .header("Authorization", "Bearer " + token)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(noteDTO)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(note.getId()))
               .andExpect(jsonPath("$.patientId").value(note.getPatientId()))
               .andExpect(jsonPath("$.note").value(note.getNote()));
    }

    @Test
    void testGetNotesByPatientId() throws Exception {
        String token = jwtUtils.generateToken("USER", 36000L);

        mockMvc.perform(get("/patient-123")
                                .header("Authorization", "Bearer " + token))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].patientId").value(note.getPatientId()))
               .andExpect(jsonPath("$[0].note").value(note.getNote()));
    }

    @Test
    void testDeleteNote() throws Exception {
        String token = jwtUtils.generateToken("USER", 36000L);

        mockMvc.perform(delete("/note-1")
                                .header("Authorization", "Bearer " + token))
               .andExpect(status().isOk())
               .andExpect(content().string("Note supprimée avec succès"));
    }

    @Test
    void testDeleteNote_notFound() throws Exception {
        String token = jwtUtils.generateToken("USER", 36000L);

        mockMvc.perform(delete("/note-999")
                                .header("Authorization", "Bearer " + token))
               .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateNote_validationError() throws Exception {
        NoteDTO invalidNoteDTO = new NoteDTO();
        invalidNoteDTO.setPatientId("patient-123");

        String token = jwtUtils.generateToken("USER", 36000L);

        mockMvc.perform(put("/note-1")
                                .header("Authorization", "Bearer " + token)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(invalidNoteDTO)))
               .andExpect(status().isBadRequest());
    }
}
