package com.shop_app.product.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilterRequest {
    private long categoryId;
    private String search;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
