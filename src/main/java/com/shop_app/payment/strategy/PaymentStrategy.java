package com.shop_app.payment.strategy;

import com.shop_app.payment.entity.Payment;
import com.shop_app.payment.enums.PaymentMethod;
import com.shop_app.payment.response.PaymentResponse;

public interface PaymentStrategy {

    PaymentMethod getMethod();

    PaymentResponse pay(Payment payment);
}
