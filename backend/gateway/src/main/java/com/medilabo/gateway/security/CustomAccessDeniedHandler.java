package com.medilabo.gateway.security;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Handles access denied exceptions for the Gateway.
 * <p>
 * Sends HTTP 403 responses with French messages when a user attempts 
 * to access a resource without sufficient permissions.
 */
@Component
public class CustomAccessDeniedHandler implements ServerAccessDeniedHandler {

    private static final Logger logger = LogManager.getLogger(CustomAccessDeniedHandler.class);

    @Autowired 
    private ErrorResponseWriter errorResponseWriter;

    /**
     * Intercepts access denied exceptions and writes a 403 Forbidden response.
     *
     * @param exchange the current server exchange
     * @param denied the exception that caused the access denial
     * @return a Mono that completes when the response has been written
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        String path = exchange.getRequest().getPath().value();
        String message = denied != null ? denied.getMessage() : "Accès interdit";

        logger.warn("Access denied for path: {}", path);
        return errorResponseWriter.write(exchange, HttpStatus.FORBIDDEN, message);
    }
}
