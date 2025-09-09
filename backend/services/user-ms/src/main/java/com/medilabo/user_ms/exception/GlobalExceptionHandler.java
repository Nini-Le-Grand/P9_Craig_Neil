package com.medilabo.user_ms.exception;

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
 * Global exception handler to manage and format API error responses consistently.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Handles validation errors.
     *
     * @param ex      the ValidationException thrown
     * @param request the HTTP request
     * @return ResponseEntity containing the ApiError
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(ValidationException ex, HttpServletRequest request) {
        logger.warn("ValidationException on {}: {}", request.getRequestURI(), ex.getErrors());

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
     * Handles requests to nonexistent routes.
     *
     * @param ex      the NoHandlerFoundException thrown
     * @param request the HTTP request
     * @return ResponseEntity containing the ApiError
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNoHandlerFound(NoHandlerFoundException ex, HttpServletRequest request) {
        logger.warn("NoHandlerFoundException on {}: {}", request.getRequestURI(), ex.getMessage());

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
     * Handles ResponseStatusException, typically thrown with custom HTTP status codes.
     *
     * @param ex      the ResponseStatusException thrown
     * @param request the HTTP request
     * @return ResponseEntity containing the ApiError
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        logger.warn("ResponseStatusException on {}: {}", request.getRequestURI(), ex.getReason());

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
     * Handles all uncaught exceptions.
     *
     * @param ex      the Exception thrown
     * @param request the HTTP request
     * @return ResponseEntity containing the ApiError
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllUncaught(Exception ex, HttpServletRequest request) {
        logger.error("Unhandled exception on {}: {}", request.getRequestURI(), ex.getMessage(), ex);

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
