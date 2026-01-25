package com.shop_app.product;

import com.shop_app.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    boolean existsByName(String name);

    @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
    List<Product> findAllByIdIn(@Param("productIds") List<Long> productIds);
}
