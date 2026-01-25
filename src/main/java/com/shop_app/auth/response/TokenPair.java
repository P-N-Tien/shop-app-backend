package com.shop_app.auth.response;

public record TokenPair(String accessToken, String refreshToken) {
    public TokenPair {
        if (accessToken == null || refreshToken == null) {
            throw new IllegalArgumentException("Token must not be null");
        }
    }
}