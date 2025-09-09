package com.medilabo.user_ms.controller;

import com.medilabo.user_ms.domain.dto.PatientDTO;
import com.medilabo.user_ms.service.PatientService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsible for handling patient-related endpoints.
 */
@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    private static final Logger logger = LogManager.getLogger(PatientController.class);

    /**
     * Retrieves all patients for the connected doctor.
     *
     * @return ResponseEntity containing a list of PatientDTO
     */
    @GetMapping
    public ResponseEntity<List<PatientDTO>> getPatients() {
        logger.info("GET /patients - Fetching all patients for connected doctor");
        List<PatientDTO> patients = patientService.getPatients();
        logger.debug("Number of patients retrieved: {}", patients.size());

        return ResponseEntity.ok(patients);
    }

    /**
     * Retrieves a specific patient by ID.
     *
     * @param id the patient ID
     * @return ResponseEntity containing the PatientDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable String id) {
        logger.info("GET /patients/{} - Fetching patient by ID", id);
        PatientDTO patientDTO = patientService.getPatient(id);
        logger.debug("Patient retrieved: {}", patientDTO);

        return ResponseEntity.ok(patientDTO);
    }

    /**
     * Adds a new patient.
     *
     * @param patientDTO patient data to create
     * @param result     binding result for validation
     * @return ResponseEntity containing the created PatientDTO
     */
    @PostMapping
    public ResponseEntity<PatientDTO> addPatient(@Valid @RequestBody PatientDTO patientDTO, BindingResult result) {
        logger.info("POST /patients - Adding new patient: {}", patientDTO);
        PatientDTO createdPatientDTO = patientService.addPatient(patientDTO, result);
        logger.debug("Patient created: {}", createdPatientDTO);

        return ResponseEntity.ok(createdPatientDTO);
    }

    /**
     * Updates an existing patient.
     *
     * @param patientDTO patient data to update
     * @param result     binding result for validation
     * @param id         patient ID to update
     * @return ResponseEntity containing the updated PatientDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@Valid @RequestBody PatientDTO patientDTO, BindingResult result, @PathVariable String id) {
        logger.info("PUT /patients/{} - Updating patient: {}", id, patientDTO);
        PatientDTO updatedPatient = patientService.updatePatient(id, patientDTO, result);
        logger.debug("Patient updated: {}", updatedPatient);

        return ResponseEntity.ok(updatedPatient);
    }

    /**
     * Deletes a patient by ID.
     *
     * @param id patient ID to delete
     * @return ResponseEntity containing a success message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable String id) {
        logger.info("DELETE /patients/{} - Deleting patient", id);
        String successMessage = patientService.deletePatient(id);
        logger.debug("Patient deletion result: {}", successMessage);

        return ResponseEntity.ok(successMessage);
    }
}
