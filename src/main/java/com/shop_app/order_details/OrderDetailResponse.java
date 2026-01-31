package com.shop_app.order_details;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderDetailResponse(
        Long id,
        String name,
        BigDecimal priceAtPurchase,
        int quantity,
        BigDecimal totalPrice,
        Long productId
) {
}