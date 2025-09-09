package com.medilabo.evaluation_ms.security;

import com.medilabo.evaluation_ms.exception.ErrorResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom AccessDeniedHandler that handles 403 Forbidden errors
 * and logs access denial events.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger logger = LogManager.getLogger(CustomAccessDeniedHandler.class);

    /**
     * Handles access denied exceptions by logging the event
     * and sending a standardized JSON error response.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param accessDeniedException the exception thrown
     * @throws IOException if writing the response fails
     */
    @Override
    public void handle(HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {

        logger.warn("Access denied for path '{}': {}", request.getRequestURI(), accessDeniedException.getMessage());

        String errorMessage = "Vous n'êtes pas autorisé à consulter cette ressource";

        ErrorResponseWriter.write(request, response, HttpStatus.FORBIDDEN, errorMessage);
    }
}
