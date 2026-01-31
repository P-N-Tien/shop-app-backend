package com.shop_app.product.entity;

import com.shop_app.category.entity.Category;
import com.shop_app.product.enums.ProductStatus;
import com.shop_app.shared.model.BaseEntity;
import com.shop_app.product.images.entity.ProductImage;
import com.shop_app.order_details.entity.OrderDetail;
import com.shop_app.inventory.entity.Inventory;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true, exclude = {"category", "orderDetails", "productImages"})
public class Product extends BaseEntity {

    @Id
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(name = "status", length = 10)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @OrderBy("sortOrder ASC")
    private List<ProductImage> productImages;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne(mappedBy = "product")
    private Inventory inventory;

    // ============ HELPER METHODS ============== //

    public void assignStock(Inventory inventory) {
        this.inventory = inventory;
        inventory.setProduct(this);
    }
}