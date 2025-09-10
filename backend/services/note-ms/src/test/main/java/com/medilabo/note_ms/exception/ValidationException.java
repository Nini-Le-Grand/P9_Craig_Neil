package com.medilabo.note_ms.exception;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom exception thrown when validation of DTO or entity fields fails.
 * Captures the field errors in a map for easy access.
 */
@Getter
public class ValidationException extends RuntimeException {

    private static final Logger logger = LogManager.getLogger(ValidationException.class);

    /**
     * Map containing field names and their respective validation error messages.
     */
    private final Map<String, String> errors;

    /**
     * Constructs a new ValidationException with the given BindingResult.
     *
     * @param result the BindingResult containing validation errors
     */
    public ValidationException(BindingResult result) {
        super("Fields contain Errors");
        this.errors = new HashMap<>();
        for (FieldError error : result.getFieldErrors()) {
            this.errors.put(error.getField(), error.getDefaultMessage());
        }

        logger.warn("ValidationException created with errors: {}", this.errors);
    }
}
