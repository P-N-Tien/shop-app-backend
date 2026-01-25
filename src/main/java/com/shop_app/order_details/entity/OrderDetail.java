package com.shop_app.order_details.entity;

import com.shop_app.product.entity.Product;
import com.shop_app.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    private Long id;

    // save the product name for history
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price_at_purchase", nullable = false)
    private BigDecimal priceAtPurchase;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public BigDecimal getTotalPrice() {
        return priceAtPurchase.multiply(new BigDecimal(quantity));
    }
}
