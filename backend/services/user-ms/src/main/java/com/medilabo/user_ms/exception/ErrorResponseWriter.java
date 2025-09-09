package com.medilabo.user_ms.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class to write consistent JSON error responses for the API.
 */
@Component
public class ErrorResponseWriter {

    private static final Logger logger = LogManager.getLogger(ErrorResponseWriter.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Writes an ApiError response to the HttpServletResponse.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param status   the HTTP status to set
     * @param message  the error message to return; if null, uses the status reason phrase
     * @throws IOException in case of IO errors while writing the response
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
                null,
                request.getRequestURI(),
                LocalDateTime.now().format(formatter)
        );

        logger.warn("Writing error response for {}: {} - {}", request.getRequestURI(), status.value(), message);

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(apiError));
    }
}
