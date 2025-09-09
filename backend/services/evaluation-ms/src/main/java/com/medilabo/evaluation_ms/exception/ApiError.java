package com.medilabo.evaluation_ms.exception;

import lombok.*;

/**
 * Represents a standardized API error response.
 * Contains details about the HTTP status, error type, message, path, and timestamp.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    /** HTTP status code (e.g., 404, 500) */
    private int status;

    /** Name of the HTTP status (e.g., NOT_FOUND, INTERNAL_SERVER_ERROR) */
    private String error;

    /** Detailed message describing the error (in French or user-facing) */
    private String message;

    /** The API path that triggered the error */
    private String path;

    /** Timestamp of when the error occurred */
    private String timestamp;
}
