package com.medilabo.note_ms.configuration;

import com.medilabo.note_ms.security.CustomAccessDeniedHandler;
import com.medilabo.note_ms.security.CustomAuthEntryPoint;
import com.medilabo.note_ms.security.CustomFilter;
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
 * Security configuration for the Note microservice.
 * Sets up JWT authentication, stateless sessions, and role-based access.
 */
@Configuration
public class SecurityConfig {

    @Autowired private CustomFilter customFilter;
    @Autowired private CustomAuthEntryPoint customAuthEntryPoint;
    @Autowired private CustomAccessDeniedHandler customAccessDeniedHandler;

    /**
     * Configures the security filter chain.
     * All endpoints require ROLE_USER and JWT authentication.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // Disable CSRF since we're using stateless JWT authentication
                .csrf(AbstractHttpConfigurer::disable)

                // Authorize requests
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").hasRole("USER")
                        .anyRequest().authenticated()
                )

                // Configure exception handling for auth errors
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )

                // Add JWT filter before username/password filter
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)

                // Use stateless sessions
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    /**
     * Password encoder bean used to hash passwords.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
