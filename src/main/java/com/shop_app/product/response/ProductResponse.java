package com.shop_app.product.response;

import com.shop_app.images.ProductImagesRequest;
import com.shop_app.product.enums.ProductStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record ProductResponse(
        long id,
        String name,
        BigDecimal price,
        long categoryId,
        String categoryName,
        String thumbnailUrl,
        ProductStatus status,
        String description,
        List<ProductImagesRequest> images
) {
}