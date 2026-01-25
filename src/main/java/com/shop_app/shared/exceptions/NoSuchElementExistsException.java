package com.shop_app.shared.exceptions;

import com.shop_app.shared.exceptions.enums.ErrorCode;

public class NoSuchElementExistsException extends BaseException {
    public NoSuchElementExistsException(String msg) {
        super(ErrorCode.NOT_FOUND, msg);
    }
}
