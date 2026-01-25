package com.shop_app.shared.exceptions;

import com.shop_app.shared.exceptions.enums.ErrorCode;
import jakarta.persistence.LockTimeoutException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PessimisticLockException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle all business exceptions
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BaseException ex, HttpServletRequest req) {
        ErrorCode errorCode = ex.getErrorCode();

        log.warn(
                "[BUSINESS_ERROR] code={}, message={}, path={}",
                errorCode.getCode(),
                ex.getMessage(),
                req.getRequestURI()
        );

        return buildResponse(errorCode, ex.getMessage(), req);
    }

    /**
     * Security - access denied
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex,
            HttpServletRequest request
    ) {
        return buildResponse(ErrorCode.FORBIDDEN, ex.getMessage(), request);
    }

    /**
     * Validation error (DTO @Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            HttpServletRequest req
    ) {
        String errorMessage = ex.getBindingResult().getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return buildResponse(ErrorCode.INVALID_PARAM, errorMessage, req);
    }

    /**
     * Database constrain violation
     */
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest req
    ) {
        log.error("[DB_ERROR]", ex);

        return buildResponse(
                ErrorCode.DUPLICATE,
                "Duplicate data or constrain violation",
                req
        );
    }

    /**
     * Fallback - unexpected system error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception ex, HttpServletRequest req) {
        log.error("[SYSTEM_ERROR]", ex);

        return buildResponse(ErrorCode.INTERNAL_ERROR, null, req);
    }

    /**
     * Conflict Lock
     */
    @ExceptionHandler({
            PessimisticLockException.class,
            LockTimeoutException.class
    })
    public ResponseEntity<?> handleLockException(Exception ex, HttpServletRequest req) {
        log.error("[LOCK_PESSIMISTIC_ERROR]", ex);

        return buildResponse(
                ErrorCode.CONFLICT,
                "System busy, please retry",
                req);
    }

    /**
     * Security: handle load user from DB and compare password
     *
     * @param BadCredentialsException:                password invalid
     * @param InternalAuthenticationServiceException: user not found or null
     */
    @ExceptionHandler(value = {
            BadCredentialsException.class,
            InternalAuthenticationServiceException.class
    })
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest req
    ) {
        log.error("[DB_ERROR]", ex);

        return buildResponse(
                ErrorCode.UNAUTHORIZED,
                "Username or password is invalid",
                req
        );
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            ErrorCode errorCode,
            String message,
            HttpServletRequest request
    ) {
        ErrorResponse errorResponse = ErrorResponse.from(
                errorCode, message, request.getRequestURI()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }
}