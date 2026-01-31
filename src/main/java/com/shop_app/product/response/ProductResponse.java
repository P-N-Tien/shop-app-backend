package com.shop_app.product.response;

import com.shop_app.product.enums.ProductStatus;
import com.shop_app.product.images.ProductImagesResponse;
import com.shop_app.product.images.entity.ProductImage;
import com.shop_app.shared.dto.BaseResponse;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ProductResponse(
        long id,
        String name,
        BigDecimal price,
        long categoryId,
        String thumbnail,
        ProductStatus status,
        String description,
        List<ProductImagesResponse> images
) {
}