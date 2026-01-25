package com.shop_app.payment.exceptions;

import com.shop_app.shared.exceptions.BaseException;
import com.shop_app.shared.exceptions.enums.ErrorCode;

public class PaymentNotFoundException extends BaseException {
    public PaymentNotFoundException(String message) {
        super(ErrorCode.NOT_FOUND, message);
    }
}
