package com.shop_app.auth.refresh_token;


import java.time.Duration;
import java.util.UUID;

public interface IRefreshTokenService {
    void save(long userId, UUID jti, Duration expiration);

    boolean exists(UUID jti);

    void revoke(UUID jti);

    int cleanupExpiredTokens();
}
