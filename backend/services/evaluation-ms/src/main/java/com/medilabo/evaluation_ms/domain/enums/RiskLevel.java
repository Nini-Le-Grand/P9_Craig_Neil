package com.medilabo.evaluation_ms.domain.enums;

import lombok.Getter;

/**
 * Enum representing the risk level of a patient.
 * Each level has a corresponding human-readable label.
 */
@Getter
public enum RiskLevel {
    /** No risk detected */
    NONE,

    /** Borderline risk detected */
    BORDERLINE,

    /** Patient is in danger */
    IN_DANGER,

    /** Early onset of condition detected */
    EARLY_ONSET;
}
