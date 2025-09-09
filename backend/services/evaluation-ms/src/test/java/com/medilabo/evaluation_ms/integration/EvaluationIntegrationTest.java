package com.medilabo.evaluation_ms.integration;

import com.medilabo.evaluation_ms.client.NoteClient;
import com.medilabo.evaluation_ms.client.PatientClient;
import com.medilabo.evaluation_ms.domain.dto.NoteDTO;
import com.medilabo.evaluation_ms.domain.dto.PatientDTO;
import com.medilabo.evaluation_ms.domain.enums.Gender;
import com.medilabo.evaluation_ms.domain.enums.RiskLevel;
import com.medilabo.evaluation_ms.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class EvaluationIntegrationTest {
    @Autowired private JwtUtils jwtUtils;
    @Autowired private MockMvc mockMvc;
    @Autowired private PatientClient patientClient;
    @Autowired private NoteClient noteClient;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PatientClient patientClient() {
            return Mockito.mock(PatientClient.class);
        }

        @Bean
        public NoteClient noteClient() {
            return Mockito.mock(NoteClient.class);
        }
    }

    @Test
    void testEvaluationEndpoint_returnsRisk() throws Exception {
        String token = jwtUtils.generateToken("USER", 36000L);

        PatientDTO patient = new PatientDTO();
        patient.setId("123");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setGender(Gender.M);

        NoteDTO note1 = new NoteDTO(); note1.setNote("Hémoglobine A1C anormale");
        NoteDTO note2 = new NoteDTO(); note2.setNote("Poids élevé");

        when(patientClient.getPatientById("123")).thenReturn(patient);
        when(noteClient.getNotesByPatientId("123")).thenReturn(List.of(note1, note2));

        mockMvc.perform(get("/evaluation/123")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isOk(),
                       content().string(RiskLevel.BORDERLINE.name())
               );
    }

    @Test
    void testEvaluationEndpoint_patientNotFound_returns500() throws Exception {
        String token = jwtUtils.generateToken("USER", 36000L);

        when(patientClient.getPatientById("999"))
                .thenThrow(new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Impossible de récupérer le patient"));

        mockMvc.perform(get("/evaluation/999")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isInternalServerError(),
                       jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                       jsonPath("$.error").value(HttpStatus.INTERNAL_SERVER_ERROR.name()),
                       jsonPath("$.message").value("Impossible de récupérer le patient"),
                       jsonPath("$.path").value("/evaluation/999")
               );
    }
}