package com.shop_app.images;

import lombok.Builder;

@Builder
public record ProductImagesRequest(
        Long id,
        Long productId,
        String imageUrl,
        Boolean isPrimary,
        Integer sortOrder
) {
}
