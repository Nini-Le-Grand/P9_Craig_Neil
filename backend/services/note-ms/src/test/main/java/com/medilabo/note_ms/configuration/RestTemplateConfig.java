package com.medilabo.note_ms.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Configuration for RestTemplate.
 * Adds an interceptor to propagate the Authorization header from the incoming HTTP request
 * to outgoing REST calls, allowing JWT authentication to be forwarded between microservices.
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .additionalInterceptors((request, body, execution) -> {
                    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    if (attr != null) {
                        HttpServletRequest servletRequest = attr.getRequest();
                        String token = servletRequest.getHeader("Authorization");
                        if (token != null) {
                            request.getHeaders().set("Authorization", token);
                        }
                    }
                    return execution.execute(request, body);
                })
                .build();
    }
}
