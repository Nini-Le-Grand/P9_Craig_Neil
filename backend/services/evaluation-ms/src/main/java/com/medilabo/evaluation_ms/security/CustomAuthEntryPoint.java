package com.medilabo.evaluation_ms.security;

import com.medilabo.evaluation_ms.exception.ErrorResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom AuthenticationEntryPoint that handles 401 Unauthorized errors
 * and logs unauthorized access attempts.
 */
@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {
    private static final Logger logger = LogManager.getLogger(CustomAuthEntryPoint.class);

    /**
     * Handles unauthorized access attempts by logging the event
     * and sending a standardized JSON error response.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param authException the exception thrown
     * @throws IOException if writing the response fails
     */
    @Override
    public void commence(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        logger.warn("Unauthorized access attempt to path '{}': {}", request.getRequestURI(), authException.getMessage());

        String errorMessage = "Vous n'êtes pas authentifié";

        ErrorResponseWriter.write(request, response, HttpStatus.UNAUTHORIZED, errorMessage);
    }
}
