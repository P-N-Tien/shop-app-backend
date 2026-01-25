package com.shop_app.payment.exceptions;

import com.shop_app.shared.exceptions.BaseException;
import com.shop_app.shared.exceptions.enums.ErrorCode;

public class PaymentException extends BaseException {
    public PaymentException(String message) {
        super(ErrorCode.BAD_REQUEST, message);
    }
}
