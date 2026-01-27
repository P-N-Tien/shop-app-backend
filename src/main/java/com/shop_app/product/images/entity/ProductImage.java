package com.shop_app.product.images.entity;

import com.shop_app.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_images_id_gen")
    @SequenceGenerator(
            name = "product_images_id_gen",
            sequenceName = "product_images_id_seq",
            allocationSize = 50,
            initialValue = 1
    )
    private Long id;

    @Column(name = "image_url", nullable = false, length = 300)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
