package com.medilabo.gateway.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Handles unauthorized access attempts for the Gateway.
 * <p>
 * Sends HTTP 401 responses with French messages when authentication fails.
 */
@Component
public class CustomAuthEntryPoint implements ServerAuthenticationEntryPoint {

    private static final Logger logger = LogManager.getLogger(CustomAuthEntryPoint.class);

    @Autowired 
    private ErrorResponseWriter errorResponseWriter;

    /**
     * Intercepts unauthorized requests and writes a 401 Unauthorized response.
     *
     * @param exchange the current server exchange
     * @param ex the exception that caused the authentication failure
     * @return a Mono that completes when the response has been written
     */
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        String path = exchange.getRequest().getPath().value();
        String message = ex != null ? ex.getMessage() : "Non autorisé";

        logger.warn("Unauthorized request to path: {}", path);
        return errorResponseWriter.write(exchange, HttpStatus.UNAUTHORIZED, message);
    }
}
