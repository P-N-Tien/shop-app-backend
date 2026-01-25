package com.shop_app.category;

import com.shop_app.category.request.CategoryRequest;
import com.shop_app.category.response.CategoryResponse;

import java.util.List;

public interface ICategoryService {
    void createCategories(List<CategoryRequest> req);

    List<CategoryResponse> findAll();

    CategoryResponse findById(long id);

    void update(long id, CategoryRequest req);

    void delete(long id);
}
