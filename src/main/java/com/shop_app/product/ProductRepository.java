package com.shop_app.product;

import com.shop_app.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    boolean existsByName(String name);

    @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
    List<Product> findAllByIdIn(@Param("productIds") List<Long> productIds);

    @Query("""
                SELECT p FROM Product p
                WHERE p.status = 'ACTIVE'
                AND p.category.id = :categoryId
            """)
    Page<Product> findByCategory(
            @Param("categoryId") Long categoryId, Pageable pageable);

    @Query("""
                SELECT p FROM Product p 
                LEFT JOIN FETCH p.productImages i 
                WHERE p.status = 'ACTIVE' 
                AND i.isPrimary = TRUE
            """)
    Page<Product> findAllActiveWithPrimaryImage(Pageable pageable);

    @Query("""
                SELECT p FROM Product p
                WHERE p.status = 'ACTIVE'
                AND p.name = :name
            """)
    Optional<Product> findByName(String name);
}
