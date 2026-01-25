package com.shop_app.configs.security.exceptions;

import com.shop_app.shared.exceptions.BaseException;
import com.shop_app.shared.exceptions.enums.ErrorCode;

public class InvalidTokenException extends BaseException {
    public InvalidTokenException(String message) {
        super(ErrorCode.UNAUTHORIZED, message);
    }
}
