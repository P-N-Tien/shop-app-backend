package com.shop_app.shared.exceptions.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // ===== CLIENT =====
    BAD_REQUEST("BAD_REQUEST", "Bad Request", HttpStatus.BAD_REQUEST),
    NOT_FOUND("NOT_FOUND", "Resource Not Found", HttpStatus.NOT_FOUND),
    DUPLICATE("DUPLICATE", "Duplicate Entry", HttpStatus.CONFLICT),
    INVALID_PARAM("INVALID_PARAM", "Invalid Request", HttpStatus.BAD_REQUEST),
    BUSINESS_ERROR(
            "BUSINESS_ERROR",
            "Business Error",
            HttpStatus.UNPROCESSABLE_ENTITY),

    // ===== SECURITY =====
    FORBIDDEN("FORBIDDEN", "Access Denied", HttpStatus.FORBIDDEN),
    UNAUTHORIZED("UNAUTHORIZED", "Unauthorized", HttpStatus.UNAUTHORIZED),

    // ===== SYSTEM =====
    INTERNAL_ERROR(
            "INTERNAL_ERROR",
            "Internal Server Error",
            HttpStatus.INTERNAL_SERVER_ERROR),
    CONFLICT(
            "CONFLICT",
            "System busy, please retry",
            HttpStatus.CONFLICT
    );

    private final String code;
    private final String message;
    private final HttpStatus status;
}
