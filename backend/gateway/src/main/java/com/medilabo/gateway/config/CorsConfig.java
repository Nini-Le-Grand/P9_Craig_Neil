package com.medilabo.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Configures CORS for the Gateway to allow requests from the frontend.
 * <p>
 * This configuration allows specific origins, HTTP methods, headers, 
 * and supports credentials for cross-origin requests.
 */
@Configuration
public class CorsConfig {

    private static final Logger logger = LogManager.getLogger(CorsConfig.class);

    /**
     * Creates and returns a CorsConfigurationSource bean that defines 
     * the CORS settings for all incoming requests.
     * <p>
     * Allowed origin: http://localhost:3000
     * Allowed methods: GET, POST, PUT, DELETE, OPTIONS
     * Allowed headers: all headers
     * Credentials: allowed
     *
     * @return a CorsConfigurationSource object with the defined CORS rules
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        logger.info("Setting up CORS configuration");

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
