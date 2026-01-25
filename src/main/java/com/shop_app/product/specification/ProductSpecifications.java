package com.shop_app.product.specification;

import com.shop_app.product.entity.Product;
import com.shop_app.product.request.ProductFilterRequest;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecifications {

    public static Specification<Product> filter(ProductFilterRequest req) {
        return Specification.where(searchInName(req.getSearch())
                .and(equalToRelationshipId("category", req.getCategoryId()))
                .and(betweenPrice(req.getMinPrice(), req.getMaxPrice())));
    }

    private static Specification<Product> searchInName(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isEmpty()) return null;
            String pattern = "%" + keyword.trim().toLowerCase() + "%";
            return cb.like(cb.lower(root.get("name")), pattern);
        };
    }

    private static Specification<Product> equalToRelationshipId(
            String relationshipField, Long value) {
        if (value == null) return null;
        return (root, query, cb) ->
                // Truy cập (root.get("category")).get("id")
                cb.equal(root.get(relationshipField).get("id"), value);
    }

    private static Specification<Product> betweenPrice(BigDecimal min, BigDecimal max) {
        if (min == null && max == null) return null;
        return (root, query, cb) -> {
            Path<BigDecimal> price = root.get("price");
            if (min != null && max != null) return cb.between(price, min, max);
            if (min != null) return cb.greaterThanOrEqualTo(price, min);
            return cb.lessThanOrEqualTo(price, max);
        };
    }
}
