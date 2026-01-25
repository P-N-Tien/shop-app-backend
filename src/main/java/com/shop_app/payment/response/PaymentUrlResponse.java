package com.shop_app.payment.response;

import java.math.BigDecimal;

public record PaymentUrlResponse(
        String paymentUrl,
        Long orderId,
        String transactionId,
        BigDecimal amount
) {
}
