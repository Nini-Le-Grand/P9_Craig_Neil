package com.medilabo.note_ms.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Global exception handler for the application.
 * Catches various exceptions and returns structured JSON responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Handles validation exceptions thrown when DTO validation fails.
     *
     * @param ex      the validation exception
     * @param request the HTTP request
     * @return ResponseEntity with ApiError and HTTP 400
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(ValidationException ex, HttpServletRequest request) {
        logger.warn("Validation error on {}: {}", request.getRequestURI(), ex.getErrors());

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                "Veuillez vérifier les données saisies",
                ex.getErrors(),
                request.getRequestURI(),
                LocalDateTime.now().format(formatter)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    /**
     * Handles requests to non-existent routes.
     *
     * @param ex      the exception
     * @param request the HTTP request
     * @return ResponseEntity with ApiError and HTTP 404
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNoHandlerFound(NoHandlerFoundException ex, HttpServletRequest request) {
        logger.warn("No handler found for request {}", request.getRequestURI());

        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.name(),
                "Route inexistante",
                null,
                request.getRequestURI(),
                LocalDateTime.now().format(formatter)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    /**
     * Handles ResponseStatusException, typically thrown by services with specific HTTP codes.
     *
     * @param ex      the exception
     * @param request the HTTP request
     * @return ResponseEntity with ApiError and the corresponding HTTP status
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        logger.warn("ResponseStatusException on {}: {} - {}", request.getRequestURI(), status, ex.getReason());

        ApiError apiError = new ApiError(
                status.value(),
                status.name(),
                ex.getReason(),
                null,
                request.getRequestURI(),
                LocalDateTime.now().format(formatter)
        );
        return ResponseEntity.status(ex.getStatusCode()).body(apiError);
    }

    /**
     * Handles any uncaught exceptions to prevent exposing internal errors.
     *
     * @param ex      the exception
     * @param request the HTTP request
     * @return ResponseEntity with ApiError and HTTP 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllUncaught(Exception ex, HttpServletRequest request) {
        logger.error("Unexpected error on {}: {}", request.getRequestURI(), ex.getMessage(), ex);

        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                "Une erreur inattendue est survenue",
                null,
                request.getRequestURI(),
                LocalDateTime.now().format(formatter)
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(apiError);
    }
}
