package com.shop_app.payment;

import com.shop_app.order.entity.Order;
import com.shop_app.payment.entity.Payment;
import com.shop_app.payment.enums.PaymentMethod;
import com.shop_app.payment.enums.PaymentStatus;
import com.shop_app.payment.response.PaymentResponse;
import com.shop_app.payment.strategy.PaymentStrategy;
import com.shop_app.payment.strategy.PaymentStrategyFactory;
import com.shop_app.shared.validate.Validate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentStrategyFactory factory;

    @Override
    public PaymentResponse processPayment(PaymentMethod method, Payment payment) {
        PaymentStrategy strategy = factory.getStrategy(method);
        return strategy.pay(payment);
    }

    @Override
    @Transactional
    public Payment createPayment(Order order) {
        Validate.requiredNonNull(order, "Order must not be null");

        PaymentStatus initialStatus = PaymentStatus.PENDING;
        String txIdPrefix = "TX-";

        if (order.getPaymentMethod() == PaymentMethod.COD) {
            // For COD, keep the status as PENDING until payment is recivied from the shipper
            initialStatus = PaymentStatus.PENDING;
            txIdPrefix = "COD-";
        }

        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalMoney())
                .status(initialStatus)
                .method(order.getPaymentMethod())
                .transactionId(txIdPrefix + UUID.randomUUID().toString().substring(0, 8))
                .build();

        paymentRepository.save(payment);

        log.info("[PAYMENT][CREATE] Create Payment successful with id={}, orderId={}, userId={}",
                payment.getId(), order.getId(), order.getUserId());

        return payment;
    }

}