package com.shop_app.inventory.entity;

import com.shop_app.product.entity.Product;
import com.shop_app.shared.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "inventories",
        uniqueConstraints = @UniqueConstraint(columnNames = "product_id")
)
@Entity
@Builder
@ToString(exclude = {"product"})
public class Inventory extends BaseEntity {

    /**
     * Enabel/Disable version when using OPTIMISTIC/PESSIMISTIC LOCK
     */
    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedQuantity;

    @Column(name = "sold_quantity", nullable = false)
    private Integer soldQuantity;

    @Column(name = "product_id", insertable = false, updatable = false)
    private Long productId;

    @OneToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    // ============ HELPER METHODS ============== //

    public void reserve(int amount) {
        this.quantity -= amount;
        this.reservedQuantity += amount;
    }

    public void releaseReserve(int amount) {
        this.quantity += amount;
        this.reservedQuantity -= amount;
    }

    public void commitSale(int amount) {
        this.reservedQuantity -= amount;
        this.soldQuantity += amount;
    }
}
