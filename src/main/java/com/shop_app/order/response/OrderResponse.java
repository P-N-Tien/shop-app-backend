package com.shop_app.order.response;

import com.shop_app.order.enums.OrderStatus;
import com.shop_app.order_details.OrderDetailResponse;
import com.shop_app.payment.enums.PaymentMethod;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderResponse(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String recipientName,
        BigDecimal totalMoney,
        String recipientPhone,
        String recipientAddress,
        String note,
        PaymentMethod paymentMethod,
        OrderStatus status,
        List<OrderDetailResponse> orderDetails
) {
    // Compact constructor để đảm bảo list không bao giờ null
    public OrderResponse {
        if (orderDetails == null) {
            orderDetails = List.of();
        }
    }
}