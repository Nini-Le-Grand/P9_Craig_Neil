package com.medilabo.evaluation_ms.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class containing trigger terms used to evaluate patient risk.
 * <p>
 * This class is final and cannot be instantiated.
 */
public final class TriggerTerms {

    /**
     * List of trigger terms used for risk evaluation.
     */
    public static final List<String> TERMS = Arrays.asList(
            "Hémoglobine A1C",
            "Microalbumine",
            "Taille",
            "Poids",
            "Fumeur",
            "Fumeuse",
            "Anormal",
            "Cholestérol",
            "Vertiges",
            "Rechute",
            "Réaction",
            "Anticorps"
    );
}
