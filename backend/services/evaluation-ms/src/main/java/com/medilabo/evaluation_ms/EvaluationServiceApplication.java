package com.medilabo.evaluation_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

/**
 * Main entry point for the Evaluation Service Spring Boot application.
 * <p>
 * The default UserDetailsService autoconfiguration is excluded.
 */
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class EvaluationServiceApplication {

	/**
	 * Application main method.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(EvaluationServiceApplication.class, args);
	}
}
