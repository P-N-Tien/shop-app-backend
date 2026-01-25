package com.shop_app.shared.exceptions;

import com.shop_app.shared.exceptions.enums.ErrorCode;

public class SystemException extends BaseException {
    public SystemException(String message) {
        super(ErrorCode.INTERNAL_ERROR, message);
    }
}
