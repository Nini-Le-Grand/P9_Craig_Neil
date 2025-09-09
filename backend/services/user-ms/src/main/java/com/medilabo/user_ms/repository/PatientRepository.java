package com.medilabo.user_ms.repository;

import com.medilabo.user_ms.domain.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing Patient entities.
 */
public interface PatientRepository extends JpaRepository<Patient, String> {

    /**
     * Retrieves all patients associated with a given doctor ID.
     *
     * @param doctorId the doctor's ID
     * @return list of patients for the doctor
     */
    List<Patient> findAllByDoctorId(String doctorId);

    /**
     * Retrieves a patient by their email address.
     *
     * @param email the patient's email
     * @return an Optional containing the patient if found
     */
    Optional<Patient> findByEmail(String email);
}
