package com.medilabo.evaluation_ms.client;

import com.medilabo.evaluation_ms.domain.dto.PatientDTO;
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
 * Client responsible for retrieving patient information
 * from the Patient microservice.
 * <p>
 * Uses {@link RestTemplate} to communicate with the external API.
 */
@Component
public class PatientClient {
    @Autowired private RestTemplate restTemplate;

    @Value("${user.ms.url}")
    private String baseUrl;

    private static final Logger logger = LogManager.getLogger(PatientClient.class);


    /**
     * Fetches a patient by its unique identifier.
     *
     * @param patientId the identifier of the patient
     * @return the corresponding {@link PatientDTO}
     * @throws ResponseStatusException if the patient cannot be retrieved
     */
    public PatientDTO getPatientById(String patientId) {
        try {
            logger.info("Requesting patient with id {}", patientId);

            PatientDTO patient = restTemplate.getForObject(
                    baseUrl + "/patients/" + patientId,
                    PatientDTO.class
            );

            if (patient == null) {
                logger.warn("No patient found with id {}", patientId);
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Impossible de récupérer le patient"
                );
            }

            logger.info("Patient retrieved: {}", patient);
            return patient;

        } catch (RestClientException e) {
            logger.error("Error while retrieving patient {}: {}", patientId, e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Impossible de récupérer le patient"
            );
        }
    }
}
