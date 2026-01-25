package com.shop_app.configs.security.exceptions;

import com.shop_app.shared.exceptions.BaseException;
import com.shop_app.shared.exceptions.enums.ErrorCode;

public class JwtException extends BaseException {
    public JwtException(String message) {
        super(ErrorCode.UNAUTHORIZED, message);
    }
}