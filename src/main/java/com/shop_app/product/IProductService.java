package com.shop_app.product;

import com.shop_app.product.request.ProductCreateRequest;
import com.shop_app.product.entity.Product;
import com.shop_app.product.request.ProductFilterRequest;
import com.shop_app.product.request.ProductPatchRequest;
import com.shop_app.product.response.ProductResponse;
import com.shop_app.shared.dto.PageResponse;
import org.springframework.data.domain.Pageable;

public interface IProductService {
    PageResponse<ProductResponse> getProducts(
            Long categoryId,
            int page,
            int limit
    );

    void create(ProductCreateRequest product);

    ProductResponse getById(long id);

    ProductResponse getByName(String name);

    void partialUpdate(long id, ProductPatchRequest product);

    void delete(long id);

    boolean existByName(String name);

    PageResponse<ProductResponse> search(
            ProductFilterRequest filter,
            Pageable pageable
    );
}
