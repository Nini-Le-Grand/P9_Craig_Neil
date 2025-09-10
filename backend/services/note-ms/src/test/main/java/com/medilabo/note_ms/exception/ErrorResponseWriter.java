package com.medilabo.note_ms.exception;

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
 * Utility class to write structured API error responses.
 * Logs the error details before sending them to the client.
 */
@Component
public class ErrorResponseWriter {

    private static final Logger logger = LogManager.getLogger(ErrorResponseWriter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Writes an error response in JSON format to the HttpServletResponse.
     * Logs the status, path, and message.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param status   the HTTP status to return
     * @param message  the error message to include (if null, uses default status reason)
     * @throws IOException if writing the response fails
     */
    public static void write(HttpServletRequest request,
                             HttpServletResponse response,
                             HttpStatus status,
                             String message) throws IOException {

        String path = request.getRequestURI();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String errorMessage = message != null ? message : status.getReasonPhrase();

        ApiError apiError = new ApiError(
                status.value(),
                status.name(),
                errorMessage,
                null,
                path,
                timestamp
        );

        logger.warn("Error response [{} {}] for path {}: {}", status.value(), status.name(), path, errorMessage);

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(apiError));
    }
}
