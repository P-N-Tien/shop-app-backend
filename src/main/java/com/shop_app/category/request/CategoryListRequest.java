package com.shop_app.category.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryListRequest {
    @NotEmpty(message = "Category list cannot be empty")
    @Valid
    private List<CategoryRequest> categories;
}
