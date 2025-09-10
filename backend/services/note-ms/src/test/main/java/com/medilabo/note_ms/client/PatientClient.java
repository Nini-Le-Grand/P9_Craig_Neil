package com.medilabo.note_ms.client;

import com.medilabo.note_ms.domain.dto.PatientDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

/**
 * Client to communicate with the User Microservice to retrieve patient data.
 * Propagates the Authorization header automatically via RestTemplate interceptor.
 */
@Component
public class PatientClient {

    private static final Logger logger = LogManager.getLogger(PatientClient.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${user.ms.url}")
    private String baseUrl;

    /**
     * Retrieve a patient by ID from the User Microservice.
     *
     * @param patientId The ID of the patient to fetch.
     * @return PatientDTO object containing patient information.
     */
    public PatientDTO getPatientById(String patientId) {
        String url = baseUrl + "/patients/" + patientId;
        logger.info("Fetching patient from User MS: {}", url);

        try {
            PatientDTO patient = restTemplate.getForObject(url, PatientDTO.class);

            if (patient == null) {
                logger.error("Received null patient from User MS for ID: {}", patientId);
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Impossible de récupérer le patient"
                );
            }

            logger.info("Successfully retrieved patient with ID: {}", patientId);
            return patient;

        } catch (RestClientException e) {
            logger.error("Error while fetching patient with ID {}: {}", patientId, e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Impossible de récupérer le patient"
            );
        }
    }
}
