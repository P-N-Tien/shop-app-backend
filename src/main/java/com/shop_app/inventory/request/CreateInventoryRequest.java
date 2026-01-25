package com.shop_app.inventory.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInventoryRequest {

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be greater or equal 1")
    private Integer quantity;

    @NotNull(message = "Product' Id is required")
    @Min(value = 1, message = "Product' Id must be greater or equal 1")
    private Long productId;
}
