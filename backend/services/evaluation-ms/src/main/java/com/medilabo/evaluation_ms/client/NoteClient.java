package com.medilabo.evaluation_ms.client;

import com.medilabo.evaluation_ms.domain.dto.NoteDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

/**
 * Client responsible for retrieving notes for a patient
 * from the Note microservice.
 * <p>
 * Uses {@link RestTemplate} to communicate with the external API.
 */
@Component
public class NoteClient {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${note.ms.url}")
    private String baseUrl;

    private static final Logger logger = LogManager.getLogger(NoteClient.class);

    /**
     * Fetches all notes associated with the given patient.
     *
     * @param patientId the identifier of the patient
     * @return a list of {@link NoteDTO} objects
     * @throws ResponseStatusException if an error occurs while calling the Note
     *                                 service
     */
    public List<NoteDTO> getNotesByPatientId(String patientId) {
        try {
            logger.info("Requesting notes for patient {}", patientId);

            NoteDTO[] notesArray = restTemplate.getForObject(
                    baseUrl + "/" + patientId,
                    NoteDTO[].class);

            if (notesArray.length == 0) {
                logger.warn("No notes found for patient {}", patientId);
                return List.of();
            }

            logger.info("Notes retrieved for patient {}: {}", patientId, Arrays.toString(notesArray));
            return Arrays.asList(notesArray);

        } catch (RestClientException e) {
            logger.error("Error while retrieving notes for patient {}: {}", patientId, e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Impossible de récupérer les notes");
        }
    }
}
