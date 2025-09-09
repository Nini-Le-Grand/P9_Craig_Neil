package com.medilabo.evaluation_ms.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Configuration class for {@link RestTemplate}.
 * <p>
 * This configuration adds an interceptor to automatically propagate
 * the "Authorization" header from the current HTTP request
 * to outgoing {@link RestTemplate} requests.
 */
@Configuration
public class RestTemplateConfig {
    private static final Logger logger = LogManager.getLogger(RestTemplateConfig.class);


    /**
     * Provides a {@link RestTemplate} bean configured with an interceptor
     * that copies the Authorization header from the incoming request.
     *
     * @return a configured {@link RestTemplate} instance
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .additionalInterceptors((request, body, execution) -> {
                    ServletRequestAttributes attr =
                            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

                    if (attr != null) {
                        HttpServletRequest servletRequest = attr.getRequest();
                        String token = servletRequest.getHeader("Authorization");

                        if (token != null) {
                            logger.info("Propagating Authorization header to outgoing request: {}", token);
                            request.getHeaders().set("Authorization", token);
                        } else {
                            logger.trace("No Authorization header found in the incoming request.");
                        }
                    } else {
                        logger.trace("No current request context available, skipping Authorization propagation.");
                    }

                    return execution.execute(request, body);
                })
                .build();
    }
}
