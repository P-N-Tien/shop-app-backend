package com.shop_app.configs.rest_template;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                // Maximum connection time
                .setConnectTimeout(Duration.ofSeconds(5))
                // Maximum response time
                .setReadTimeout(Duration.ofSeconds(10))
                // Log request/response
                .additionalInterceptors(new RestTemplateLoggingInterceptor())
                // Centralized error handling
                .errorHandler(new RestTemplateErrorHandler())
                .build();
    }
}