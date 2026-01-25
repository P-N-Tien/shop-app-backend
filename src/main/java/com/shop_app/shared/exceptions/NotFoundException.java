package com.shop_app.shared.exceptions;

import com.shop_app.shared.exceptions.enums.ErrorCode;

public class NotFoundException extends BaseException {
    public NotFoundException(String msg) {
        super(ErrorCode.NOT_FOUND, msg);
    }
}