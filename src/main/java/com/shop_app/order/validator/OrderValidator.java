package com.shop_app.order.validator;

import com.shop_app.order.OrderRepository;
import com.shop_app.order.entity.Order;
import com.shop_app.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderValidator {
    private final OrderRepository repository;

    public Order validateAndGet(long id) {
        return repository
                .findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Order not found with id: " + id));
    }
}
