package com.shop_app.payment;

import com.shop_app.order.entity.Order;
import com.shop_app.payment.entity.Payment;
import com.shop_app.payment.enums.PaymentMethod;
import com.shop_app.payment.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(PaymentMethod method, Payment payment);

    Payment createPayment(Order order);
}
