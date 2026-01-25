package com.shop_app.auth.refresh_token;

import com.shop_app.shared.exceptions.NotFoundException;
import com.shop_app.shared.validate.Validate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService implements IRefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public void save(long userId, UUID jti, Duration expiration) {
        // 1. Input validation
        validateSaveParameters(userId, jti, expiration);

        // 2. Create a new refresh token
        RefreshToken refreshToken = RefreshToken.builder()
                .jti(jti)
                .userId(userId)
                .expiresAt(Instant.now().plus(expiration))
                .revoked(false)
                .build();

        // 3. Save Db
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public boolean exists(UUID jti) {
        Validate.requiredNonNull(jti, "JWT ID must not be null");

        return refreshTokenRepository.findById(jti)
                // The token has not been revoked and has not expired
                .map(token -> !token.isRevoked() && !isExpired(token))
                .orElse(false);
    }

    @Override
    @Transactional
    public void revoke(UUID jti) {
        Validate.requiredNonNull(jti, "JWT ID must not be null");

        RefreshToken refreshToken = refreshTokenRepository.findById(jti)
                .orElseThrow(() -> new NotFoundException("Refresh token not found"));

        // Idempotent check
        if (refreshToken.isRevoked()) {
            log.info("Token already revoked. JTI: {}, UserID: {}", jti, refreshToken.getUserId());
            return;
        }

        // Log if trying to revoke expired token
        if (isExpired(refreshToken)) {
            log.info("Revoking already expired token. JTI: {}, UserID: {}, ExpiredAt: {}",
                    jti, refreshToken.getUserId(), refreshToken.getExpiresAt());
        }

        // Perform revocation
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        log.info("Refresh token revoked successfully. JTI: {}, UserID: {}",
                jti, refreshToken.getUserId());
    }

    @Override
    public int cleanupExpiredTokens() {
        Instant now = Instant.now();
        int deletedCount = refreshTokenRepository.deleteExpiredAndRevoked(now);
        return deletedCount;
    }

    /**
     * Validates parameters for save operation.
     */
    private void validateSaveParameters(long userId, UUID jti, Duration expiration) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        if (jti == null) {
            throw new IllegalArgumentException("JTI cannot be null");
        }
        if (expiration == null || expiration.isNegative() || expiration.isZero()) {
            throw new IllegalArgumentException("Expiration must be positive duration");
        }
    }

    /**
     * Checks if a token is expired.
     */
    private boolean isExpired(RefreshToken token) {
        return token.getExpiresAt().isBefore(Instant.now());
    }
}
