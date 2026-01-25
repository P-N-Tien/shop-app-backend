package com.shop_app.payment.strategy;

import com.shop_app.payment.enums.PaymentMethod;
import com.shop_app.payment.exceptions.PaymentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PaymentStrategyFactory {
    private final Map<PaymentMethod, PaymentStrategy> strategyMap;

    public PaymentStrategyFactory(List<PaymentStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        PaymentStrategy::getMethod,
                        Function.identity()
                ));
    }

    public PaymentStrategy getStrategy(PaymentMethod method) {
        PaymentStrategy strategy = strategyMap.get(method);
        if (strategy == null) {
            throw new PaymentException(
                    "Payment method not supported: " + method
            );
        }
        return strategy;
    }
}
