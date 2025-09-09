package com.medilabo.evaluation_ms.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Global exception handler that catches unhandled exceptions and maps them to
 * standardized JSON error responses. Logs exception details using Log4j2.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Handles 404 errors when no handler is found for the request.
     *
     * @param ex      the exception thrown
     * @param request the HTTP request
     * @return a ResponseEntity with status 404 and ApiError body
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNoHandlerFound(NoHandlerFoundException ex, HttpServletRequest request) {
        logger.warn("No handler found for path '{}': {}", request.getRequestURI(), ex.getMessage());

        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.name(),
                "Route inexistante",
                request.getRequestURI(),
                LocalDateTime.now().format(formatter)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(apiError);
    }

    /**
     * Handles exceptions of type ResponseStatusException.
     *
     * @param ex      the exception thrown
     * @param request the HTTP request
     * @return a ResponseEntity with the corresponding status and ApiError body
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());

        logger.warn("ResponseStatusException for path '{}': {} {}", request.getRequestURI(), status.value(), ex.getReason());

        ApiError apiError = new ApiError(
                status.value(),
                status.name(),
                ex.getReason(),
                request.getRequestURI(),
                LocalDateTime.now().format(formatter)
        );
        return ResponseEntity.status(ex.getStatusCode())
                             .body(apiError);
    }

    /**
     * Handles all uncaught exceptions.
     *
     * @param ex      the exception thrown
     * @param request the HTTP request
     * @return a ResponseEntity with status 500 and ApiError body
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllUncaught(Exception ex, HttpServletRequest request) {
        logger.error("Unhandled exception for path '{}': {}", request.getRequestURI(), ex.getMessage(), ex);

        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                "Une erreur inattendue est survenue",
                request.getRequestURI(),
                LocalDateTime.now().format(formatter)
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                             .body(apiError);
    }
}
