package com.shop_app.product;

import com.shop_app.product.request.ProductCreateRequest;
import com.shop_app.product.entity.Product;
import com.shop_app.product.request.ProductFilterRequest;
import com.shop_app.product.request.ProductPatchRequest;
import com.shop_app.product.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.io.IOException;
import java.util.List;

public interface IProductService {
    //    ProductsPagingResponse getAll(Pageable pageable);
    void create(ProductCreateRequest product);

    ProductResponse getById(long id);

    void partialUpdate(long id, ProductPatchRequest product);

    void delete(long id);

    boolean existByName(String name);

    Page<ProductResponse> search(
            ProductFilterRequest filter,
            Pageable pageable
    );

    List<Product> findAllByIdInList(List<Long> productIds);
}
