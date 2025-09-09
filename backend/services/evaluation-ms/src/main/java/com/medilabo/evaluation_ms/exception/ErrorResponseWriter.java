package com.medilabo.evaluation_ms.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for writing standardized JSON error responses to the HTTP response.
 * Uses {@link ApiError} as the response body and logs the error details.
 */
@Component
public class ErrorResponseWriter {
    private static final Logger logger = LogManager.getLogger(ErrorResponseWriter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Writes an error response as JSON to the HTTP response.
     *
     * @param request  the incoming HTTP request
     * @param response the HTTP response to write to
     * @param status   the HTTP status code to return
     * @param message  the error message (if null, the default status reason will be used)
     * @throws IOException if writing to the response fails
     */
    public static void write(HttpServletRequest request,
            HttpServletResponse response,
            HttpStatus status,
            String message) throws IOException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ApiError apiError = new ApiError(
                status.value(),
                status.name(),
                message != null ? message : status.getReasonPhrase(),
                request.getRequestURI(),
                LocalDateTime.now().format(formatter)
        );

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        logger.info("Error response for path '{}': {} {}", request.getRequestURI(), status.value(), message);

        response.getWriter().write(objectMapper.writeValueAsString(apiError));
    }
}
