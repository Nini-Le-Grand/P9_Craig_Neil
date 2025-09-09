package com.medilabo.user_ms.security;

import com.medilabo.user_ms.exception.ErrorResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LogManager.getLogger(CustomAuthEntryPoint.class);

    /**
     * Handles unauthorized access attempts.
     *
     * @param request       the HTTP request
     * @param response      the HTTP response
     * @param authException the authentication exception thrown
     * @throws IOException in case of IO errors
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
