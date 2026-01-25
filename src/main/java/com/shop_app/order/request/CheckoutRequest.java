package com.shop_app.order.request;


import com.shop_app.payment.enums.PaymentMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;


@Builder
public record CheckoutRequest(
        @NotNull(message = "User' Id is required")
        @Min(value = 1, message = "User' Id must be greater or equal 1")
        Long userId,

        @NotBlank(message = "Recipient Name is required")
        String recipientName,

        @NotBlank(message = "Recipient Phone is required")
        String recipientPhone,

        @NotBlank(message = "Recipient Address is required")
        String recipientAddress,

        String note,

        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod,

        @NotNull(message = "Items is not empty")
        List<ItemRequest> items
) {
    public CheckoutRequest {
        if (items == null) {
            items = new ArrayList<>();
        }
    }
}
