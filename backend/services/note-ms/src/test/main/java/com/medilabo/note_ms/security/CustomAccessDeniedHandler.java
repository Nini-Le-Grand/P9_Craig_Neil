package com.medilabo.note_ms.security;

import com.medilabo.note_ms.exception.ErrorResponseWriter;
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
 * Handles access denied situations by returning a 403 response.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = LogManager.getLogger(CustomAccessDeniedHandler.class);

    /**
     * Handles a forbidden access attempt.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @param accessDeniedException exception that caused the access denial
     * @throws IOException if writing the response fails
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        String errorMessage = "Vous n'êtes pas autorisés à consulter cette ressource";

        logger.warn("Access denied to {}: {}", request.getRequestURI(), accessDeniedException.getMessage());

        ErrorResponseWriter.write(request, response, HttpStatus.FORBIDDEN, errorMessage);
    }
}
