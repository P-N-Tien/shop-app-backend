package com.shop_app.order.response;

import com.shop_app.payment.enums.PaymentMethod;
import com.shop_app.payment.enums.PaymentStatus;

/**
 * "orderId": 1002,
 * "paymentMethod": "VNPAY",
 * "paymentStatus": "PENDING",
 * "paymentUrl": "https://sandbox.vnpayment.vn/...",
 * "message": "Redirect to VNPay to make payment"
 */
public record CheckoutResponse(
        Long orderId,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        String paymentUrl,      // chỉ có khi cần redirect
        String message
) {
}
