package com.shop_app.payment.method.vn_pay;

import com.shop_app.payment.entity.Payment;
import com.shop_app.payment.enums.PaymentMethod;
import com.shop_app.payment.response.PaymentResponse;
import com.shop_app.payment.strategy.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VNPayPaymentStrategy implements PaymentStrategy {

    private final VNPayService vnPayService;

    @Override
    public PaymentMethod getMethod() {
        return PaymentMethod.VNPAY;
    }

    @Override
    public PaymentResponse pay(Payment request) {
        return vnPayService.createPaymentUrl(request);
    }

}
