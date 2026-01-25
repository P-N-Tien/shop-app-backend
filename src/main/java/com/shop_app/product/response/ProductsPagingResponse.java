package com.shop_app.product.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductsPagingResponse {
    private List<ProductResponse> data;
    private int total;
    private int page;
}

