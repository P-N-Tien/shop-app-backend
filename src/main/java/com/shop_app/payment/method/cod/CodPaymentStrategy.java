package com.shop_app.payment.method.cod;

import com.shop_app.payment.entity.Payment;
import com.shop_app.payment.enums.PaymentMethod;
import com.shop_app.payment.enums.PaymentStatus;
import com.shop_app.payment.response.PaymentResponse;
import com.shop_app.payment.strategy.PaymentStrategy;
import org.springframework.stereotype.Component;

@Component
public class CodPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentMethod getMethod() {
        return PaymentMethod.COD;
    }

    @Override
    public PaymentResponse pay(Payment request) {
        return PaymentResponse.builder()
                .status(PaymentStatus.SUCCESS)
                .message("Order placed successfully. Payment upon delivery.")
                .paymentUrl("")
                .build();
    }
}
