package com.shop_app.order.exceptions;

import com.shop_app.shared.exceptions.BaseException;
import com.shop_app.shared.exceptions.enums.ErrorCode;

public class ProductNotAvailableException extends BaseException {
    public ProductNotAvailableException(String msg) {
        super(ErrorCode.BUSINESS_ERROR, msg);
    }
}
