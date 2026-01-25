package com.shop_app.shared.exceptions;

import com.shop_app.shared.exceptions.enums.ErrorCode;
import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {
    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode, String customMsg) {
        super(customMsg);
        this.errorCode = errorCode;
    }
}
