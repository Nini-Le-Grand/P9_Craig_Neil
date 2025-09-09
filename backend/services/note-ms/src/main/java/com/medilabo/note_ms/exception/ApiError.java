package com.medilabo.note_ms.exception;

import lombok.*;
import java.util.Map;

/**
 * Represents a structured API error response.
 * Used to return meaningful error messages and validation details to clients.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    /** HTTP status code of the error (e.g., 400, 404, 500). */
    private int status;

    /** Short name of the HTTP status (e.g., BAD_REQUEST, NOT_FOUND). */
    private String error;

    /** Human-readable error message, typically shown to the client. */
    private String message;

    /** Field-level validation errors, if applicable (field name → error message). */
    private Map<String, String> fieldErrors;

    /** Request URI that caused the error. */
    private String path;

    /** Timestamp when the error occurred in format yyyy-MM-dd HH:mm:ss. */
    private String timestamp;
}
