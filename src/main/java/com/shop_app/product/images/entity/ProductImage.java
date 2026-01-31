package com.shop_app.product.images.entity;

import com.shop_app.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "product_images")
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "product_images_id_gen")
    @SequenceGenerator(
            name = "product_images_id_gen",
            sequenceName = "product_images_id_seq",
            allocationSize = 50,
            initialValue = 1
    )
    private Long id;

    @Column(name = "sort_order")
    private int sortOrder = 0;

    @Column(name = "image_url", nullable = false, length = 300)
    private String imageUrl;

    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary = false;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
