package com.medilabo.evaluation_ms.controller;

import com.medilabo.evaluation_ms.domain.enums.RiskLevel;
import com.medilabo.evaluation_ms.service.EvaluationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for evaluating patient diabetes risk level.
 * <p>
 * Exposes endpoints to retrieve the risk level of a patient
 * based on their medical notes and personal information.
 */
@RestController
public class EvaluationController {
    private static final Logger logger = LogManager.getLogger(EvaluationController.class);
    @Autowired private EvaluationService evaluationService;

    /**
     * Endpoint to evaluate the diabetes risk of a patient.
     *
     * @param patientId ID of the patient to evaluate
     * @return {@link ResponseEntity} containing the risk level label
     */
    @GetMapping("/{patientId}")
    public ResponseEntity<RiskLevel> evaluatePatientRisk(@PathVariable String patientId) {
        logger.info("Received request to evaluate risk for patientId={}", patientId);

        RiskLevel riskLevel = evaluationService.evaluateRisk(patientId);

        logger.info("Calculated risk level for patientId={} is {}", patientId, riskLevel);
        return ResponseEntity.ok(riskLevel);
    }
}
