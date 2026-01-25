package com.shop_app.order.exceptions;

import com.shop_app.shared.exceptions.BaseException;
import com.shop_app.shared.exceptions.enums.ErrorCode;

public class OutOfInventoryException extends BaseException {
    public OutOfInventoryException(String msg) {
        super(ErrorCode.BUSINESS_ERROR, msg);
    }
}
