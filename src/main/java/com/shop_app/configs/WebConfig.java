package com.shop_app.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Check business logic
 */
@Configuration
@RequiredArgsConstructor
@EnableRetry(order = 1)
@EnableTransactionManagement(order = 2)
public class WebConfig implements WebMvcConfigurer {

//    private final IdempotencyInterceptor idempotencyInterceptor;

    @Value("${api.prefix}")
    private String baseURL;

    /**
     * Register the Idempotency Interceptor for specific POST/PUT/PATCH endpoints
     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(idempotencyInterceptor)
//                .addPathPatterns(getIdempotencyPaths());
//    }

    /**
     * Construct paths dynamically to ensure ${api.prefix} is properly injected.
     */
    private List<String> getIdempotencyPaths() {
        return List.of(
                baseURL + "/orders/checkout",
                baseURL + "/categories/bulk"
        );
    }
}