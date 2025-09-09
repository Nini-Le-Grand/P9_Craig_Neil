package com.medilabo.gateway.config;

import com.medilabo.gateway.security.CustomAccessDeniedHandler;
import com.medilabo.gateway.security.CustomFilter;
import com.medilabo.gateway.security.CustomAuthEntryPoint;
import static com.medilabo.gateway.security.Roles.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Configures the security for the Gateway using Spring WebFlux Security.
 * <p>
 * JWT authentication is applied using a custom filter. Access rules
 * are defined per route path.
 */
@Configuration
public class SecurityConfig {

    private static final Logger logger = LogManager.getLogger(SecurityConfig.class);

    @Autowired private CustomFilter customFilter;
    @Autowired private CustomAuthEntryPoint customAuthEntryPoint;
    @Autowired private CustomAccessDeniedHandler customAccessDeniedHandler;

    /**
     * Defines the security filter chain for all incoming requests.
     *
     * @param http the ServerHttpSecurity object
     * @return the configured SecurityWebFilterChain
     */
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        logger.info("Initializing SecurityWebFilterChain for Gateway");
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/frontend/**").permitAll()
                        .pathMatchers("/.well-known/appspecific/com.chrome.devtools.json").permitAll()
                        .pathMatchers("/api/users/auth/login").permitAll()
                        .pathMatchers("/api/users/**").hasAnyRole(USER, ADMIN)
                        .pathMatchers("/api/users/admin/**").hasRole(ADMIN)
                        .pathMatchers("/api/notes/**").hasRole(USER)
                        .pathMatchers("/api/evaluation/**").hasRole(USER)
                        .anyExchange().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler))
                .addFilterAt(customFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
