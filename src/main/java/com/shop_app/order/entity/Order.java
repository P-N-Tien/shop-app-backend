package com.shop_app.order.entity;

import com.shop_app.order.enums.OrderStatus;
import com.shop_app.payment.entity.Payment;
import com.shop_app.payment.enums.PaymentMethod;
import com.shop_app.shared.model.BaseEntity;
import com.shop_app.order_details.entity.OrderDetail;
import com.shop_app.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "orders",
        indexes = {
                @Index(name = "idx_order_user", columnList = "user_id"),
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order extends BaseEntity {

    @Id
    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @Column(name = "total_money", nullable = false)
    private BigDecimal totalMoney = BigDecimal.ZERO;

    @Column(name = "recipient_name", nullable = false, length = 100)
    private String recipientName;

    @Column(name = "recipient_phone", nullable = false, length = 15)
    private String recipientPhone;

    @Column(name = "recipient_address", nullable = false, length = 255)
    private String recipientAddress;

    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "payment_method", length = 100)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "status", length = 20)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<OrderDetail> orderDetails = new ArrayList<>();

    // not allow set, only read from db
    @Column(name = "user_id", insertable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Long userId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(mappedBy = "order")
    private Payment payment;

    // =========== HELPER METHODS =========== //

    /**
     * Binding 2 direction
     */
    public void addOrderDetail(OrderDetail detail) {
        this.orderDetails.add(detail);
        detail.setOrder(this);
    }
}
