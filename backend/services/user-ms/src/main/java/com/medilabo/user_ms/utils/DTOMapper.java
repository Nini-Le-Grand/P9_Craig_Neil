package com.medilabo.user_ms.utils;

import com.medilabo.user_ms.domain.dto.PatientDTO;
import com.medilabo.user_ms.domain.dto.UserDTO;
import com.medilabo.user_ms.domain.entity.Patient;
import com.medilabo.user_ms.domain.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Utility service for mapping between entities and DTOs for users and patients.
 */
@Service
public class DTOMapper {

    private static final Logger logger = LogManager.getLogger(DTOMapper.class);

    /**
     * Maps a User entity to a UserDTO.
     *
     * @param user The User entity
     * @return UserDTO with values copied from the entity
     */
    public UserDTO userToDTO(User user) {
        logger.info("Mapping User entity to UserDTO for user ID {}", user.getId());

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setDateOfBirth(user.getDateOfBirth());
        userDTO.setPhone(user.getPhone());
        userDTO.setGender(user.getGender());
        userDTO.setEmail(user.getEmail());
        userDTO.setAddress(user.getAddress());
        userDTO.setRole(user.getRole());

        logger.info("UserDTO mapping complete for user ID {}", user.getId());
        return userDTO;
    }

    /**
     * Maps a UserDTO to an existing User entity.
     *
     * @param user    The existing User entity
     * @param userDTO DTO containing updated values
     * @return Updated User entity
     */
    public User dtoToUser(User user, UserDTO userDTO) {
        logger.info("Mapping UserDTO to User entity for user ID {}", user.getId());

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setPhone(userDTO.getPhone());
        user.setGender(userDTO.getGender());
        user.setEmail(userDTO.getEmail());
        user.setAddress(userDTO.getAddress());

        logger.info("User entity updated from UserDTO for user ID {}", user.getId());
        return user;
    }

    /**
     * Maps a Patient entity to a PatientDTO.
     *
     * @param patient The Patient entity
     * @return PatientDTO with values copied from the entity
     */
    public PatientDTO patientToDTO(Patient patient) {
        logger.info("Mapping Patient entity to PatientDTO for patient ID {}", patient.getId());

        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(patient.getId());
        patientDTO.setFirstName(patient.getFirstName());
        patientDTO.setLastName(patient.getLastName());
        patientDTO.setEmail(patient.getEmail());
        patientDTO.setGender(patient.getGender());
        patientDTO.setDateOfBirth(patient.getDateOfBirth());
        patientDTO.setAddress(patient.getAddress());
        patientDTO.setPhone(patient.getPhone());
        patientDTO.setDoctorId(patient.getDoctor().getId());

        logger.info("PatientDTO mapping complete for patient ID {}", patient.getId());
        return patientDTO;
    }

    /**
     * Maps a PatientDTO to an existing Patient entity and sets the doctor.
     *
     * @param patient    The existing Patient entity
     * @param patientDTO DTO containing updated values
     * @param doctor     Doctor User entity to assign to the patient
     * @return Updated Patient entity
     */
    public Patient dtoToPatient(Patient patient, PatientDTO patientDTO, User doctor) {
        logger.info("Mapping PatientDTO to Patient entity for patient ID {}", patient.getId());

        patient.setFirstName(patientDTO.getFirstName());
        patient.setLastName(patientDTO.getLastName());
        patient.setEmail(patientDTO.getEmail());
        patient.setGender(patientDTO.getGender());
        patient.setDateOfBirth(patientDTO.getDateOfBirth());
        patient.setAddress(patientDTO.getAddress());
        patient.setPhone(patientDTO.getPhone());
        patient.setDoctor(doctor);

        logger.info("Patient entity updated from PatientDTO for patient ID {}", patient.getId());
        return patient;
    }
}
