package com.shop_app.product.mapper;

import com.shop_app.product.entity.Product;
import com.shop_app.product.request.ProductCreateRequest;
import com.shop_app.product.request.ProductPatchRequest;
import com.shop_app.product.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductMapper {
    @Mapping(target = "images", source = "productImages")
    @Mapping(target = "categoryId", source = "category.id")
    ProductResponse toResponse(Product product);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateFrom(ProductPatchRequest req, @MappingTarget Product entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    Product createFrom(ProductCreateRequest req);
}