package com.medilabo.user_ms.service;

import com.medilabo.user_ms.domain.dto.PatientDTO;
import com.medilabo.user_ms.domain.entity.Patient;
import com.medilabo.user_ms.domain.entity.User;
import com.medilabo.user_ms.repository.PatientRepository;
import com.medilabo.user_ms.utils.DTOMapper;
import com.medilabo.user_ms.utils.DTOValidation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DTOValidation dtoValidation;

    @Autowired
    private DTOMapper dtoMapper;

    private static final Logger logger = LogManager.getLogger(PatientService.class);

    /**
     * Retrieves a patient by ID.
     *
     * @param id the ID of the patient
     * @return the Patient entity
     * @throws ResponseStatusException if the patient is not found
     */
    public Patient getPatientById(String id) {
        logger.info("Fetching patient with ID: {}", id);

        return patientRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMessage = "Patient could not be found";
                    logger.warn("Patient not found with ID: {}", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, errorMessage);
                });
    }

    /**
     * Retrieves all patients of the connected user (doctor).
     *
     * @return list of PatientDTOs
     */
    public List<PatientDTO> getPatients() {
        User connectedUser = userService.getConnectedUser();
        logger.info("Fetching all patients for doctor ID: {}", connectedUser.getId());

        List<Patient> patients = patientRepository.findAllByDoctorId(connectedUser.getId());
        return patients.stream()
                       .map(dtoMapper::patientToDTO)
                       .toList();
    }

    /**
     * Retrieves a specific patient of the connected user.
     *
     * @param id the ID of the patient
     * @return the PatientDTO
     * @throws ResponseStatusException if the user is not authorized
     */
    public PatientDTO getPatient(String id) {
        User connectedUser = userService.getConnectedUser();
        logger.info("Fetching patient ID: {} for user ID: {}", id, connectedUser.getId());

        Patient patient = getPatientById(id);

        if (!patient.getDoctor().equals(connectedUser)) {
            String errorMessage = "User is not authorized to access this patient";
            logger.warn("User ID: {} unauthorized to access patient ID: {}", connectedUser.getId(), id);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, errorMessage);
        }

        return dtoMapper.patientToDTO(patient);
    }

    /**
     * Adds a new patient for the connected user.
     *
     * @param patientDTO the patient data
     * @param result     binding result for validation
     * @return the created PatientDTO
     */
    public PatientDTO addPatient(PatientDTO patientDTO, BindingResult result) {
        logger.info("Adding new patient with email: {} by user ID: {}", patientDTO.getEmail(), patientDTO.getDoctorId());

        dtoValidation.checkPatientCreationEmailIsValid(patientDTO, result);
        dtoValidation.validateBindingResult(result);

        User connectedUser = userService.getConnectedUser();
        User doctor = userService.getUser(patientDTO.getDoctorId());

        if (!connectedUser.equals(doctor)) {
            String errorMessage = "User is not authorized to add this patient";
            logger.warn("User ID: {} unauthorized to add patient for doctor ID: {}", connectedUser.getId(), doctor.getId());
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, errorMessage);
        }

        Patient patient = dtoMapper.dtoToPatient(new Patient(), patientDTO, doctor);
        return savePatient(patient);
    }

    /**
     * Updates an existing patient.
     *
     * @param id         the patient ID
     * @param patientDTO the updated patient data
     * @param result     binding result for validation
     * @return the updated PatientDTO
     */
    public PatientDTO updatePatient(String id, PatientDTO patientDTO, BindingResult result) {
        User connectedUser = userService.getConnectedUser();
        logger.info("Updating patient ID: {} by user ID: {}", id, connectedUser.getId());

        Patient patient = getPatientById(id);

        dtoValidation.checkPatientUpdateEmailIsValid(patientDTO, patient, result);
        dtoValidation.validateBindingResult(result);

        User doctor = patient.getDoctor();
        if (!connectedUser.equals(doctor)) {
            String errorMessage = "User is not authorized to update this patient";
            logger.warn("User ID: {} unauthorized to update patient ID: {}", connectedUser.getId(), id);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, errorMessage);
        }

        patient = dtoMapper.dtoToPatient(patient, patientDTO, doctor);
        return savePatient(patient);
    }

    /**
     * Deletes a patient.
     *
     * @param id the patient ID
     * @return success message in French
     * @throws ResponseStatusException if the user is not authorized or an internal error occurs
     */
    public String deletePatient(String id) {
        User connectedUser = userService.getConnectedUser();
        logger.info("Deleting patient ID: {} by user ID: {}", id, connectedUser.getId());

        Patient patient = getPatientById(id);
        User doctor = patient.getDoctor();

        if (!connectedUser.equals(doctor)) {
            String errorMessage = "User is not authorized to delete this patient";
            logger.warn("User ID: {} unauthorized to delete patient ID: {}", connectedUser.getId(), id);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, errorMessage);
        }

        try {
            patientRepository.delete(patient);
            logger.info("Patient deleted successfully with ID: {}", id);
            return "Le patient a été supprimé avec succès";
        } catch (Exception e) {
            String errorMessage = "Erreur interne lors de la suppréssion du patient";
            logger.error("Error deleting patient ID {}: {}", id, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        }
    }

    /**
     * Saves a patient entity and returns its DTO.
     *
     * @param patient the patient entity to save
     * @return the saved PatientDTO
     * @throws ResponseStatusException in case of internal error
     */
    public PatientDTO savePatient(Patient patient) {
        try {
            patientRepository.save(patient);
            logger.info("Patient saved successfully with ID: {}", patient.getId());
            return dtoMapper.patientToDTO(patient);
        } catch (Exception e) {
            String errorMessage = "Erreur interne lors de la sauvegarde du patient";
            logger.error("Error saving patient ID {}: {}", patient.getId(), e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        }
    }
}
