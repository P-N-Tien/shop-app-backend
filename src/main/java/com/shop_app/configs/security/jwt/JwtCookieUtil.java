package com.shop_app.configs.security.jwt;

import com.shop_app.configs.security.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtCookieUtil {
    private final JwtProperties jwtProperties;

    public String addAccessTokenCookie(String token) {
        return createCookie(TokenType.ACCESS, token);
    }

    public String addRefreshTokenCookie(String token) {
        return createCookie(TokenType.REFRESH, token);
    }

    public String removeAccessTokenCookie() {
        return deleteCookie(TokenType.ACCESS);
    }

    public String removeRefreshTokenCookie() {
        return deleteCookie(TokenType.REFRESH);
    }

    /**
     * Factory method to create Cookie standard RFC 6265
     */
    public String createCookie(TokenType type, String value) {
        var config = type.getConfigMapper().apply(jwtProperties);
        return ResponseCookie.from(config.cookieName(), value)
                .httpOnly(config.httpOnly())
                .secure(config.secure())
                .path(config.path())
                .maxAge(config.expiry())
                .sameSite(config.sameSite())
                .build()
                .toString();
    }

    public String deleteCookie(TokenType type) {
        var config = type.getConfigMapper().apply(jwtProperties);
        byte TIME_TO_LIVE = 0;

        return ResponseCookie.from(config.cookieName(), "")
                .httpOnly(config.httpOnly())
                .secure(config.secure())
                .path(config.path())
                .maxAge(TIME_TO_LIVE) // Request browser delete all cookies immediately
                .sameSite(config.sameSite())
                .build()
                .toString();
    }
}