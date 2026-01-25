package com.shop_app.payment.entity;

import com.shop_app.order.entity.Order;
import com.shop_app.payment.enums.PaymentMethod;
import com.shop_app.payment.enums.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "method", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "transaction_id", nullable = false, length = 50)
    private String transactionId;

    @Column(name = "bank_code", length = 50)
    private String bankCode;

    @Column(name = "card_type", length = 50)
    private String cardType;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "response_code", length = 10)
    private String responseCode;

    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}