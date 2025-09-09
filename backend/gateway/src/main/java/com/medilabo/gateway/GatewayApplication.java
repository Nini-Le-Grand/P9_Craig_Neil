package com.medilabo.gateway;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;

/**
 * Entry point for the Spring Boot Gateway application.
 * <p>
 * The {@link ReactiveUserDetailsServiceAutoConfiguration} is excluded because
 * user management and authentication are handled via custom JWT, 
 * not via the default reactive user service.
 */
@SpringBootApplication(exclude = { ReactiveUserDetailsServiceAutoConfiguration.class })
public class GatewayApplication {

    private static final Logger logger = LogManager.getLogger(GatewayApplication.class);

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        logger.info("Starting Medilabo Gateway Application...");
        SpringApplication.run(GatewayApplication.class, args);
        logger.info("Medilabo Gateway Application started successfully.");
    }
}
