package com.shop_app.product.images;

import com.shop_app.product.entity.Product;
import com.shop_app.product.images.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findProductImageByProduct(Product product);

    int countByProduct(Product product);
}
