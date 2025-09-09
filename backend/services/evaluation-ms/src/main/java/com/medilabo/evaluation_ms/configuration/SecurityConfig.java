package com.medilabo.evaluation_ms.configuration;

import com.medilabo.evaluation_ms.client.PatientClient;
import com.medilabo.evaluation_ms.security.CustomAccessDeniedHandler;
import com.medilabo.evaluation_ms.security.CustomAuthEntryPoint;
import com.medilabo.evaluation_ms.security.CustomFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for the application.
 * <p>
 * Defines HTTP security rules, authentication/authorization handlers,
 * and registers custom filters.
 */
@Configuration
public class SecurityConfig {
    private static final Logger logger = LogManager.getLogger(SecurityConfig.class);
    @Autowired private CustomFilter customFilter;
    @Autowired private CustomAuthEntryPoint customAuthEntryPoint;
    @Autowired private CustomAccessDeniedHandler customAccessDeniedHandler;

    /**
     * Defines the main security filter chain for HTTP requests.
     *
     * @param http the {@link HttpSecurity} configuration
     * @return configured {@link SecurityFilterChain}
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring security filter chain...");

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    logger.info("Setting access rules: /evaluation/** requires ROLE_USER");
                    auth.requestMatchers("/**").hasRole("USER")
                        .anyRequest().authenticated();
                })
                .exceptionHandling(ex -> {
                    logger.info("Registering custom authenticationEntryPoint and accessDeniedHandler");
                    ex.authenticationEntryPoint(customAuthEntryPoint)
                      .accessDeniedHandler(customAccessDeniedHandler);
                })
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sess -> {
                    logger.info("Setting session management policy: STATELESS");
                    sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .build();
    }

    /**
     * Provides a password encoder bean using BCrypt.
     *
     * @return {@link BCryptPasswordEncoder}
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        logger.debug("Registering BCryptPasswordEncoder bean");
        return new BCryptPasswordEncoder();
    }
}
