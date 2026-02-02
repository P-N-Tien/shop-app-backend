package com.shop_app.images.mapper;

import com.shop_app.images.ProductImagesRequest;
import com.shop_app.images.entity.ProductImage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring") // Để dùng @Autowired trong Spring Boot
public interface ProductImageMapper {
    ProductImagesRequest toResponse(ProductImage entity);

    ProductImage toEntity(ProductImagesRequest response);

    List<ProductImagesRequest> toResponseList(List<ProductImage> entities);

    List<ProductImage> toEntityList(List<ProductImagesRequest> responses);
}
