package com.shop_app.payment.response;

import com.shop_app.payment.enums.PaymentStatus;

import java.math.BigDecimal;

public record PaymentCallbackResponse(
        Long orderId,
        String transactionId,
        BigDecimal amount,
        PaymentStatus status,
        String code,
        String message
) {
}
