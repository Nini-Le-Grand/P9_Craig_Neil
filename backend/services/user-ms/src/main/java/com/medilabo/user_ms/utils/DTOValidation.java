package com.medilabo.user_ms.utils;

import com.medilabo.user_ms.domain.dto.PasswordDTO;
import com.medilabo.user_ms.domain.dto.PatientDTO;
import com.medilabo.user_ms.domain.dto.UserDTO;
import com.medilabo.user_ms.domain.entity.Patient;
import com.medilabo.user_ms.domain.enums.Role;
import com.medilabo.user_ms.domain.entity.User;
import com.medilabo.user_ms.exception.ValidationException;
import com.medilabo.user_ms.repository.PatientRepository;
import com.medilabo.user_ms.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Optional;

/**
 * Utility service for validating DTOs related to users and patients.
 * Methods may throw {@link ValidationException} or add errors to the BindingResult.
 */
@Service
public class DTOValidation {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    private static final Logger logger = LogManager.getLogger(DTOValidation.class);

    /**
     * Validates the BindingResult and throws a ValidationException if any errors are present.
     *
     * @param result BindingResult to validate
     */
    public void validateBindingResult(BindingResult result) {
        if (result.hasErrors()) {
            logger.warn("Validation failed: {}", result.getAllErrors());
            throw new ValidationException(result);
        }
    }

    /**
     * Checks that the email of a new user is not already used.
     *
     * @param userDTO DTO of the user
     * @param result  BindingResult to register errors
     */
    public void checkUserCreationEmailIsValid(UserDTO userDTO, BindingResult result) {
        Optional<User> existing = userRepository.findByEmail(userDTO.getEmail());
        if (existing.isPresent()) {
            logger.warn("User creation email already in use: {}", userDTO.getEmail());
            result.rejectValue("email", "400", "Cet email est déjà utilisé");
        }
    }

    /**
     * Checks that the email of an existing user during update does not conflict.
     *
     * @param userDTO   DTO of the user
     * @param userEmail Current email of the user
     * @param result    BindingResult to register errors
     */
    public void checkUserUpdateEmailIsValid(UserDTO userDTO, String userEmail, BindingResult result) {
        Optional<User> existing = userRepository.findByEmail(userDTO.getEmail());
        if (existing.isPresent()) {
            User existingUser = existing.get();
            if (!existingUser.getEmail().equals(userEmail)) {
                logger.warn("User update email already in use: {}", userDTO.getEmail());
                result.rejectValue("email", "400", "Cet email est déjà utilisé");
            }
        }
    }

    /**
     * Checks that the email of a new patient is not already used.
     *
     * @param patientDTO DTO of the patient
     * @param result     BindingResult to register errors
     */
    public void checkPatientCreationEmailIsValid(PatientDTO patientDTO, BindingResult result) {
        Optional<Patient> existing = patientRepository.findByEmail(patientDTO.getEmail());
        if (existing.isPresent()) {
            logger.warn("Patient creation email already in use: {}", patientDTO.getEmail());
            result.rejectValue("email", "400", "Cet email est déjà utilisé");
        }
    }

    /**
     * Checks that the email of an existing patient during update does not conflict.
     *
     * @param patientDTO DTO of the patient
     * @param patient    Existing patient entity
     * @param result     BindingResult to register errors
     */
    public void checkPatientUpdateEmailIsValid(PatientDTO patientDTO, Patient patient, BindingResult result) {
        Optional<Patient> existing = patientRepository.findByEmail(patientDTO.getEmail());
        if (existing.isPresent()) {
            Patient existingPatient = existing.get();
            if (!existingPatient.getEmail().equals(patient.getEmail())) {
                logger.warn("Patient update email already in use: {}", patientDTO.getEmail());
                result.rejectValue("email", "400", "Cet email est déjà utilisé");
            }
        }
    }

    /**
     * Checks that the doctor associated with a patient exists and has the role USER.
     *
     * @param patientDTO DTO of the patient
     * @param result     BindingResult to register errors
     */
    public void checkDoctorExists(PatientDTO patientDTO, BindingResult result) {
        Optional<User> existing = userRepository.findById(patientDTO.getDoctorId());
        if (existing.isEmpty() || !existing.get().getRole().equals(Role.USER)) {
            logger.warn("Doctor does not exist or role invalid for ID: {}", patientDTO.getDoctorId());
            result.rejectValue("doctorId", "400", "Le docteur n'existe pas");
        }
    }

    /**
     * Checks if the provided password matches the user's current password.
     *
     * @param passwordDTO DTO containing the passwords
     * @param user        The user entity
     * @param result      BindingResult to register errors
     */
    public void checkPasswordAuth(PasswordDTO passwordDTO, User user, BindingResult result) {
        logger.info("Checking current password for user ID {}", user.getId());
        if (!passwordEncoder.matches(passwordDTO.getCurrentPassword(), user.getPassword())) {
            logger.warn("Incorrect current password for user ID {}", user.getId());
            result.rejectValue("currentPassword", "400", "Mot de passe incorrecte");
        } else {
            logger.info("Current password is correct for user ID {}", user.getId());
        }
    }

    /**
     * Checks that the new password matches the required format.
     *
     * @param passwordDTO DTO containing the new password
     * @param result      BindingResult to register errors
     */
    public void checkPasswordFormat(PasswordDTO passwordDTO, BindingResult result) {
        logger.info("Checking format of new password");
        String regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]).{6,}$";
        if (!passwordDTO.getNewPassword().matches(regexp)) {
            logger.warn("New password format is invalid");
            result.rejectValue("newPassword", "400", "Format du mot de passe invalide");
        } else {
            logger.info("New password format is valid");
        }
    }

    /**
     * Checks that the new password matches its confirmation.
     *
     * @param passwordDTO DTO containing the new password and confirmation
     * @param result      BindingResult to register errors
     */
    public void checkConfirmPassword(PasswordDTO passwordDTO, BindingResult result) {
        logger.info("Checking if new password matches confirmation");
        if (!passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
            logger.warn("New password and confirmation do not match");
            result.rejectValue("confirmPassword", "400", "Les mots de passe sont différents");
        } else {
            logger.info("New password matches confirmation");
        }
    }
}
