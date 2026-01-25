package com.shop_app.shared.exceptions;

import com.shop_app.shared.exceptions.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private String code;
    private String message;
    private Instant timestamp;
    private String path;

    ErrorResponse(ErrorCode errorCode, String message) {
        this.code = errorCode.getCode();
        this.message = message;
    }

    public static ErrorResponse from(
            ErrorCode errorCode,
            String message,
            String path
    ) {
        return ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(message != null ? message : errorCode.getMessage())
                .timestamp(Instant.now())
                .path(path)
                .build();
    }
}
