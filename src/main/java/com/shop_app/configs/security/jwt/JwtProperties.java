package com.shop_app.configs.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secret,
        Duration accessExpiration,
        Duration refreshExpiration,

        TokenConfig access,
        TokenConfig refresh
) {
    public record TokenConfig(
            String cookieName,
            Duration expiry,
            String path,
            String sameSite,
            Boolean httpOnly,
            Boolean secure
    ) {
    }
}