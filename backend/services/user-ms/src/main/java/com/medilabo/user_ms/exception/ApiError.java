package com.medilabo.user_ms.exception;

import lombok.*;

import java.util.Map;

/**
 * Standard structure for API error responses.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    /**
     * HTTP status code.
     */
    private int status;

    /**
     * HTTP status name.
     */
    private String error;

    /**
     * Human-readable error message.
     */
    private String message;

    /**
     * Map of field-specific validation errors, if any.
     */
    private Map<String, String> fieldErrors;

    /**
     * The request URI where the error occurred.
     */
    private String path;

    /**
     * Timestamp of the error occurrence in 'yyyy-MM-dd HH:mm:ss' format.
     */
    private String timestamp;
}
