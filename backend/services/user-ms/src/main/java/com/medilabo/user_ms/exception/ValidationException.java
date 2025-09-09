package com.medilabo.user_ms.exception;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom exception to handle validation errors.
 */
@Getter
public class ValidationException extends RuntimeException {

    private static final Logger logger = LogManager.getLogger(ValidationException.class);

    /**
     * Map containing field names and corresponding validation error messages.
     */
    private final Map<String, String> errors;

    /**
     * Constructs a ValidationException based on a BindingResult.
     *
     * @param result the binding result containing validation errors
     */
    public ValidationException(BindingResult result) {
        super("Fields contain Errors");
        this.errors = new HashMap<>();

        for (FieldError error : result.getFieldErrors()) {
            this.errors.put(error.getField(), error.getDefaultMessage());
        }

        logger.warn("Validation failed with errors: {}", this.errors);
    }
}
