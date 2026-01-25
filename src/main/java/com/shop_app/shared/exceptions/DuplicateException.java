package com.shop_app.shared.exceptions;

import com.shop_app.shared.exceptions.enums.ErrorCode;

public class DuplicateException extends BaseException {
    public DuplicateException(String msg) {
        super(ErrorCode.DUPLICATE, msg);
    }
}