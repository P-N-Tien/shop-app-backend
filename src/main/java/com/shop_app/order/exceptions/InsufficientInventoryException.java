package com.shop_app.order.exceptions;

import com.shop_app.shared.exceptions.BaseException;
import com.shop_app.shared.exceptions.enums.ErrorCode;

public class InsufficientInventoryException extends BaseException {
    public InsufficientInventoryException(String msg) {
        super(ErrorCode.BUSINESS_ERROR, msg);
    }
}
