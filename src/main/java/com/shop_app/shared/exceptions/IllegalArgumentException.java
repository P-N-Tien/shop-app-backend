package com.shop_app.shared.exceptions;

import com.shop_app.shared.exceptions.enums.ErrorCode;

public class IllegalArgumentException extends BaseException {
    public IllegalArgumentException(String msg) {
        super(ErrorCode.INVALID_PARAM, msg);
    }
}
