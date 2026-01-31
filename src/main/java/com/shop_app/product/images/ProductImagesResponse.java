package com.shop_app.product.images;

import lombok.Builder;

@Builder
public record ProductImagesResponse(
        Long id,
        String imageUrl,
        Boolean isPrimary,
        Integer sortOrder
) {
}
