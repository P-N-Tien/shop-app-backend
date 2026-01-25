package com.shop_app.shared.exceptions;

import com.shop_app.shared.exceptions.enums.ErrorCode;

public class InvalidParamException extends BaseException {
    public InvalidParamException(String msg) {
        super(ErrorCode.INVALID_PARAM, msg);
    }
}