package com.shop_app.configs.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop_app.shared.exceptions.ErrorResponse;
import com.shop_app.shared.exceptions.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;
    private final MessageSource messageSource;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        // 1. Log error (Sử dụng SLF4J/Logback)
        log.error("Unauthorized error: {}", accessDeniedException.getMessage());

        // 2. Get language by Header "Accept-Language"
        Locale locale = LocaleContextHolder.getLocale();
        String errorMessage = messageSource.getMessage("auth.unauthorized", null, locale);

        // 3. Building Response
        ErrorResponse apiResponse = ErrorResponse.from(
                ErrorCode.FORBIDDEN,
                errorMessage,
                request.getRequestURI()
        );

        // 4. Return JSON
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}