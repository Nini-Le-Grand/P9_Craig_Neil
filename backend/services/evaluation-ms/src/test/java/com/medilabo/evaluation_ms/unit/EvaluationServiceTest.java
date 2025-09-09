package com.medilabo.evaluation_ms.unit;

import com.medilabo.evaluation_ms.client.NoteClient;
import com.medilabo.evaluation_ms.client.PatientClient;
import com.medilabo.evaluation_ms.domain.dto.NoteDTO;
import com.medilabo.evaluation_ms.domain.dto.PatientDTO;
import com.medilabo.evaluation_ms.domain.enums.Gender;
import com.medilabo.evaluation_ms.domain.enums.RiskLevel;
import com.medilabo.evaluation_ms.service.EvaluationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EvaluationServiceTest {

    @Mock private NoteClient noteClient;
    @Mock private PatientClient patientClient;
    @InjectMocks private EvaluationService evaluationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenNoNotes_thenRiskIsNone() {
        String patientId = "1";
        PatientDTO patient = new PatientDTO();
        patient.setDateOfBirth(LocalDate.of(1980, 1, 1));
        patient.setGender(Gender.M);

        when(patientClient.getPatientById(patientId)).thenReturn(patient);
        when(noteClient.getNotesByPatientId(patientId)).thenReturn(List.of());

        RiskLevel risk = evaluationService.evaluateRisk(patientId);
        assertEquals(RiskLevel.NONE, risk);
    }

    @Test
    void whenFewTriggersAgeOver30_thenRiskIsBorderline() {
        String patientId = "2";
        PatientDTO patient = new PatientDTO();
        patient.setDateOfBirth(LocalDate.of(1980, 1, 1));
        patient.setGender(Gender.M);

        NoteDTO note1 = new NoteDTO(); note1.setNote("Hémoglobine A1C normale");
        NoteDTO note2 = new NoteDTO(); note2.setNote("Poids anormal");

        when(patientClient.getPatientById(patientId)).thenReturn(patient);
        when(noteClient.getNotesByPatientId(patientId)).thenReturn(List.of(note1, note2));

        RiskLevel risk = evaluationService.evaluateRisk(patientId);
        assertEquals(RiskLevel.BORDERLINE, risk);
    }

    @Test
    void whenMediumTriggersAgeOver30_thenRiskIsInDanger() {
        String patientId = "2";
        PatientDTO patient = new PatientDTO();
        patient.setDateOfBirth(LocalDate.of(1980, 1, 1));
        patient.setGender(Gender.M);

        NoteDTO note1 = new NoteDTO(); note1.setNote("Hémoglobine A1C normale");
        NoteDTO note2 = new NoteDTO(); note2.setNote("Poids anormal");
        NoteDTO note3 = new NoteDTO(); note3.setNote("Cholestérol");
        NoteDTO note4 = new NoteDTO(); note4.setNote("Vertiges");
        NoteDTO note5 = new NoteDTO(); note5.setNote("Anormal");
        NoteDTO note6 = new NoteDTO(); note6.setNote("Microalbumine");

        when(patientClient.getPatientById(patientId)).thenReturn(patient);
        when(noteClient.getNotesByPatientId(patientId)).thenReturn(List.of(note1, note2, note3, note4, note5, note6));

        RiskLevel risk = evaluationService.evaluateRisk(patientId);
        assertEquals(RiskLevel.IN_DANGER, risk);
    }

    @Test
    void whenManyTriggersAgeOver30_thenRiskIsInDanger() {
        String patientId = "2";
        PatientDTO patient = new PatientDTO();
        patient.setDateOfBirth(LocalDate.of(1980, 1, 1));
        patient.setGender(Gender.M);

        NoteDTO note1 = new NoteDTO(); note1.setNote("Hémoglobine A1C normale");
        NoteDTO note2 = new NoteDTO(); note2.setNote("Poids anormal");
        NoteDTO note3 = new NoteDTO(); note3.setNote("Cholestérol");
        NoteDTO note4 = new NoteDTO(); note4.setNote("Vertiges");
        NoteDTO note5 = new NoteDTO(); note5.setNote("Anormal");
        NoteDTO note6 = new NoteDTO(); note6.setNote("Microalbumine");
        NoteDTO note7 = new NoteDTO(); note7.setNote("Réaction");
        NoteDTO note8 = new NoteDTO(); note8.setNote("Taille");

        when(patientClient.getPatientById(patientId)).thenReturn(patient);
        when(noteClient.getNotesByPatientId(patientId)).thenReturn(List.of(note1, note2, note3, note4, note5, note6, note7, note8));

        RiskLevel risk = evaluationService.evaluateRisk(patientId);
        assertEquals(RiskLevel.EARLY_ONSET, risk);
    }

    @Test
    void whenFewTriggersMaleUnder30_thenRiskIsInDanger() {
        String patientId = "3";
        PatientDTO patient = new PatientDTO();
        patient.setDateOfBirth(LocalDate.now().minusYears(25));
        patient.setGender(Gender.M);

        NoteDTO note1 = new NoteDTO(); note1.setNote("Hémoglobine A1C");
        NoteDTO note2 = new NoteDTO(); note2.setNote("Poids");
        NoteDTO note3 = new NoteDTO(); note3.setNote("Cholestérol");
        NoteDTO note4 = new NoteDTO(); note4.setNote("Vertiges");


        when(patientClient.getPatientById(patientId)).thenReturn(patient);
        when(noteClient.getNotesByPatientId(patientId))
                .thenReturn(List.of(note1, note2, note3, note4));

        RiskLevel risk = evaluationService.evaluateRisk(patientId);
        assertEquals(RiskLevel.IN_DANGER, risk);
    }

    @Test
    void whenManyTriggersMaleUnder30_thenRiskIsEarlyOnset() {
        String patientId = "3";
        PatientDTO patient = new PatientDTO();
        patient.setDateOfBirth(LocalDate.now().minusYears(25));
        patient.setGender(Gender.M);

        NoteDTO note1 = new NoteDTO(); note1.setNote("Hémoglobine A1C");
        NoteDTO note2 = new NoteDTO(); note2.setNote("Poids");
        NoteDTO note3 = new NoteDTO(); note3.setNote("Cholestérol");
        NoteDTO note4 = new NoteDTO(); note4.setNote("Vertiges");
        NoteDTO note5 = new NoteDTO(); note5.setNote("Anormal");


        when(patientClient.getPatientById(patientId)).thenReturn(patient);
        when(noteClient.getNotesByPatientId(patientId))
                .thenReturn(List.of(note1, note2, note3, note4, note5));

        RiskLevel risk = evaluationService.evaluateRisk(patientId);
        assertEquals(RiskLevel.EARLY_ONSET, risk);
    }

    @Test
    void whenFewTriggersFemaleUnder30_thenRiskIsInDanger() {
        String patientId = "3";
        PatientDTO patient = new PatientDTO();
        patient.setDateOfBirth(LocalDate.now().minusYears(25));
        patient.setGender(Gender.F);

        NoteDTO note1 = new NoteDTO(); note1.setNote("Hémoglobine A1C");
        NoteDTO note2 = new NoteDTO(); note2.setNote("Poids");
        NoteDTO note3 = new NoteDTO(); note3.setNote("Cholestérol");
        NoteDTO note4 = new NoteDTO(); note4.setNote("Vertiges");


        when(patientClient.getPatientById(patientId)).thenReturn(patient);
        when(noteClient.getNotesByPatientId(patientId))
                .thenReturn(List.of(note1, note2, note3, note4));

        RiskLevel risk = evaluationService.evaluateRisk(patientId);
        assertEquals(RiskLevel.IN_DANGER, risk);
    }

    @Test
    void whenManyTriggersFemaleUnder30_thenRiskIsInDanger() {
        String patientId = "3";
        PatientDTO patient = new PatientDTO();
        patient.setDateOfBirth(LocalDate.now().minusYears(25));
        patient.setGender(Gender.M);

        NoteDTO note1 = new NoteDTO(); note1.setNote("Hémoglobine A1C");
        NoteDTO note2 = new NoteDTO(); note2.setNote("Poids");
        NoteDTO note3 = new NoteDTO(); note3.setNote("Cholestérol");
        NoteDTO note4 = new NoteDTO(); note4.setNote("Vertiges");
        NoteDTO note5 = new NoteDTO(); note5.setNote("Anormal");
        NoteDTO note6 = new NoteDTO(); note6.setNote("Microalbumine");
        NoteDTO note7 = new NoteDTO(); note7.setNote("Réaction");


        when(patientClient.getPatientById(patientId)).thenReturn(patient);
        when(noteClient.getNotesByPatientId(patientId))
                .thenReturn(List.of(note1, note2, note3, note4, note5, note6, note7));

        RiskLevel risk = evaluationService.evaluateRisk(patientId);
        assertEquals(RiskLevel.EARLY_ONSET, risk);
    }
}
