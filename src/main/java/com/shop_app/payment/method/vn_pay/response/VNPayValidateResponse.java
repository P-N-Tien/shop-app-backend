package com.shop_app.payment.method.vn_pay.response;

import com.shop_app.payment.enums.PaymentStatus;

public record VNPayValidateResponse(
        boolean success,
        String message,
        PaymentStatus paymentStatus
) {
}
