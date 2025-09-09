package com.medilabo.evaluation_ms.unit;

import com.medilabo.evaluation_ms.service.EvaluationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NormalizationTest {

    private EvaluationService evaluationService;
    private Method countTriggerTermsMethod;

    @BeforeEach
    void setup() throws NoSuchMethodException {
        evaluationService = new EvaluationService();
        countTriggerTermsMethod = EvaluationService.class
                .getDeclaredMethod("countTriggerTerms", String.class);
        countTriggerTermsMethod.setAccessible(true);
    }

    private int countTriggers(String note) throws Exception {
        return (int) countTriggerTermsMethod.invoke(evaluationService, note);
    }

    @Test
    void testAccentInsensitive() throws Exception {
        String note = "Hemoglobine A1C et Hémoglobine a1c";
        int count = countTriggers(note);
        assertEquals(2, count);
    }

    @Test
    void testCaseInsensitive() throws Exception {
        String note = "poids POIDS PoIds";
        int count = countTriggers(note);
        assertEquals(3, count);
    }

    @Test
    void testPunctuationIgnored() throws Exception {
        String note = "Cholestérol... Cholesterol!!! Cholestérol";
        int count = countTriggers(note);
        assertEquals(3, count);
    }

    @Test
    void testMultipleTriggers() throws Exception {
        String note = "Hémoglobine A1C Poids Cholestérol Vertiges Poids";
        int count = countTriggers(note);
        assertEquals(5, count);
    }

    @Test
    void testEmptyOrNullNote() throws Exception {
        assertEquals(0, countTriggers(""));
        assertEquals(0, countTriggers(null));
    }

    @Test
    void testTermWithinWord() throws Exception {
        String note = "Microalbuminer";
        int count = countTriggers(note);
        assertEquals(1, count);
    }
}
