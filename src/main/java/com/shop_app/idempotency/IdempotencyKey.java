package com.shop_app.idempotency;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(
        name = "idempotency_keys",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"key_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdempotencyKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key_id", nullable = false)
    private String keyId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private IdempotencyStatus status;

    /**
     * Save the result of request successfully
     */
    @Column(columnDefinition = "TEXT")
    private String responseBody;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    public enum IdempotencyStatus {
        PENDING,
        COMPLETED,
        FAILED,
        EXPIRED
    }
}