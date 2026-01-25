package com.shop_app.order.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ItemRequest {
    @NotNull(message = "Product' Id is required")
    @Min(value = 1, message = "Quantity must greater or equal 1")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must greater or equal 1")
    private Integer quantity;
}