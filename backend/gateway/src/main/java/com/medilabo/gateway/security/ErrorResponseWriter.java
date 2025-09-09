package com.medilabo.gateway.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Utility component for writing standardized JSON error responses.
 * <p>
 * Used by {@link CustomAuthEntryPoint} and {@link CustomAccessDeniedHandler}.
 */
@Component
public final class ErrorResponseWriter {

    private static final Logger logger = LogManager.getLogger(ErrorResponseWriter.class);

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Writes a JSON error response with HTTP status, message, timestamp, and request path.
     *
     * @param exchange current server web exchange
     * @param status   HTTP status to return
     * @param message  error message (optional, can be null)
     * @return Mono that completes when the response is written
     */
    public Mono<Void> write(ServerWebExchange exchange, HttpStatus status, String message) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
                "status", status.value(),
                "error", status.name(),
                "message", message != null ? message : status.getReasonPhrase(),
                "timestamp", LocalDateTime.now().toString(),
                "path", exchange.getRequest().getPath().value()
        );

        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsString(body).getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("Failed to serialize error response", e);
            return exchange.getResponse().setComplete();
        }

        logger.info("Writing error response: {} {}", status.value(), message);
        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(bytes))
        );
    }
}
