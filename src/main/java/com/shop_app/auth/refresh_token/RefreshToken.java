package com.shop_app.auth.refresh_token;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "refresh_tokens",
        indexes = @Index(name = "idx_user_id", columnList = "user_id"))
public class RefreshToken {
    @Id
    @Column(name = "jti", updatable = false, nullable = false)
    private UUID jti;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Builder.Default
    @Column(name = "revoked", nullable = false)
    private boolean revoked = false;
}