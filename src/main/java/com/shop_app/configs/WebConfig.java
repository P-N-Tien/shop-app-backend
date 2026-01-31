package com.shop_app.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Check business logic
 */
@Configuration
@RequiredArgsConstructor
@EnableRetry(order = 1)
@EnableTransactionManagement(order = 2)
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.file-upload.upload-dir}")
    private String uploadDir;
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


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Get absolute path
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
        String resourcePath = uploadPath.toUri().toString();

        // Map URL /uploads/**
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourcePath);

        System.out.println("[WEB-CONFIG] Static resources mapped from: " + resourcePath);
    }
}