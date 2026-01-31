package com.shop_app.payment.response;

import com.shop_app.payment.enums.PaymentStatus;
import lombok.Builder;

@Builder
public record PaymentResponse(
        PaymentStatus status,
        String url,
        String message
) {

}
