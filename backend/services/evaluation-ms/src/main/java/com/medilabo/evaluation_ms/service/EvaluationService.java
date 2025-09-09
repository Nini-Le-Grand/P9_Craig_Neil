package com.medilabo.evaluation_ms.service;

import com.medilabo.evaluation_ms.client.NoteClient;
import com.medilabo.evaluation_ms.client.PatientClient;
import com.medilabo.evaluation_ms.domain.dto.NoteDTO;
import com.medilabo.evaluation_ms.domain.dto.PatientDTO;
import com.medilabo.evaluation_ms.domain.enums.Gender;
import com.medilabo.evaluation_ms.domain.enums.RiskLevel;
import com.medilabo.evaluation_ms.utils.TriggerTerms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

/**
 * Service to evaluate the medical risk level of a patient based on notes and
 * patient data.
 */
@Service
public class EvaluationService {
    private static final Logger logger = LogManager.getLogger(EvaluationService.class);
    @Autowired
    private NoteClient noteClient;
    @Autowired
    private PatientClient patientClient;

    /**
     * Evaluates the risk level for a given patient.
     *
     * @param patientId the ID of the patient
     * @return calculated RiskLevel
     */
    public RiskLevel evaluateRisk(String patientId) {
        logger.info("Evaluating risk for patientId={}", patientId);

        PatientDTO patient = patientClient.getPatientById(patientId);
        List<NoteDTO> notes = noteClient.getNotesByPatientId(patientId);

        if (notes.isEmpty()) {
            logger.info("No notes found for patientId={}. Returning NONE", patientId);
            return RiskLevel.NONE;
        }

        int triggerCount = notes.stream()
                .mapToInt(note -> countTriggerTerms(note.getNote()))
                .sum();

        int age = Period.between(patient.getDateOfBirth(), LocalDate.now()).getYears();

        logger.info("PatientId={} | Age={} | TriggerCount={}", patientId, age, triggerCount);

        RiskLevel risk = determineRisk(patient.getGender(), age, triggerCount);

        logger.info("PatientId={} | RiskLevel={}", patientId, risk);

        return risk;
    }

    /**
     * Counts trigger terms present in a note.
     *
     * @param note note text
     * @return number of triggers found
     */
    private int countTriggerTerms(String note) {
        if (note == null || note.isBlank()) return 0;
        String normalizedNote = normalizeForMatching(note);
        int count = 0;
        for (String term : TriggerTerms.TERMS) {
            String normalizedTerm = normalizeForMatching(term);
            if (normalizedNote.contains(normalizedTerm)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Normalizes text for trigger term matching.
     *
     * @param text input text
     * @return normalized text
     */
    private String normalizeForMatching(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        normalized = normalized.replaceAll("[^a-zA-Z0-9]", "");
        return normalized.toLowerCase();
    }

    /**
     * Determines risk level based on gender, age, and trigger count.
     *
     * @param gender       patient gender
     * @param age          patient age
     * @param triggerCount number of triggers
     * @return calculated RiskLevel
     */
    private RiskLevel determineRisk(Gender gender, int age, int triggerCount) {
        boolean isMale = gender.equals(Gender.M);

        if (age < 30) {
            if (isMale) {
                if (triggerCount >= 3 && triggerCount < 5)
                    return RiskLevel.IN_DANGER;
                if (triggerCount >= 5)
                    return RiskLevel.EARLY_ONSET;
            } else {
                if (triggerCount >= 4 && triggerCount < 7)
                    return RiskLevel.IN_DANGER;
                if (triggerCount >= 7)
                    return RiskLevel.EARLY_ONSET;
            }
        } else {
            if (triggerCount >= 2 && triggerCount <= 5)
                return RiskLevel.BORDERLINE;
            if (triggerCount >= 6 && triggerCount <= 7)
                return RiskLevel.IN_DANGER;
            if (triggerCount >= 8)
                return RiskLevel.EARLY_ONSET;
        }

        return RiskLevel.NONE;
    }
}
