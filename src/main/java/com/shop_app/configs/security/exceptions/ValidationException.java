package com.shop_app.configs.security.exceptions;

import com.shop_app.shared.exceptions.BaseException;
import com.shop_app.shared.exceptions.enums.ErrorCode;

public class ValidationException extends BaseException {
    public ValidationException(String message) {
        super(ErrorCode.UNAUTHORIZED, message);
    }
}