package com.medilabo.user_ms.security;

import com.medilabo.user_ms.exception.ErrorResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = LogManager.getLogger(CustomAccessDeniedHandler.class);

    /**
     * Handles access denied (forbidden) errors.
     *
     * @param request                 the HTTP request
     * @param response                the HTTP response
     * @param accessDeniedException   the exception thrown when access is denied
     * @throws IOException in case of IO errors
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
