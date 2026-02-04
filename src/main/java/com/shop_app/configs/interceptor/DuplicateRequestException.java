package com.shop_app.configs.interceptor;

import com.shop_app.shared.exceptions.BaseException;
import com.shop_app.shared.exceptions.enums.ErrorCode;

public class DuplicateRequestException extends BaseException {
    public DuplicateRequestException(String message) {
        super(ErrorCode.DUPLICATE, message);
    }
}
