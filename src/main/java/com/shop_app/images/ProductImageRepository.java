package com.shop_app.images;

import com.shop_app.product.entity.Product;
import com.shop_app.images.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    int countByProduct(Product product);
}
