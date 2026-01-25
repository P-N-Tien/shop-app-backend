package com.shop_app.shipment.entity;

import com.shop_app.shipment.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipments")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ShipmentStatus status;

    @Column(name = "carrier")
    private String carrier;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "shipping_fee")
    private BigDecimal shippingFee;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Column(name = "estimated_delivery")
    private LocalDateTime estimatedDelivery;
}
