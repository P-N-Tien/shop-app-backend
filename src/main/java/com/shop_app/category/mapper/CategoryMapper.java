package com.shop_app.category.mapper;

import com.shop_app.category.request.CategoryRequest;
import com.shop_app.category.response.CategoryResponse;
import com.shop_app.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);

    @Mapping(target = "id", ignore = true)
    Category createFrom(CategoryRequest req);

    void updateFrom(CategoryRequest req, @MappingTarget Category category);
}
