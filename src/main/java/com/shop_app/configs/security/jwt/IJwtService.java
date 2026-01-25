package com.shop_app.configs.security.jwt;

import com.shop_app.configs.security.UserPrincipal;

public interface IJwtService {

    String generateAccessToken(UserPrincipal user);

    String generateRefreshToken(UserPrincipal user);

    boolean isTokenWellFormed(String token);

    String extractUsername(String token);

    boolean isAccessTokenValid(String token, UserPrincipal user);

    boolean isRefreshTokenValid(String token);
}