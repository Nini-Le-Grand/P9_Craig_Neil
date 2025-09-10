package com.medilabo.note_ms.security;

import com.medilabo.note_ms.exception.ErrorResponseWriter;
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
 * Handles unauthorized access attempts by returning a 401 response.
 */
@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LogManager.getLogger(CustomAuthEntryPoint.class);

    /**
     * Commences an authentication scheme.
     *
     * @param request       HTTP request
     * @param response      HTTP response
     * @param authException authentication exception that caused this entry point to be triggered
     * @throws IOException if writing the response fails
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        String errorMessage = "Vous n'êtes pas authentifiés";

        logger.warn("Unauthorized access attempt to {}: {}", request.getRequestURI(), authException.getMessage());

        ErrorResponseWriter.write(request, response, HttpStatus.UNAUTHORIZED, errorMessage);
    }
}
