package com.shop_app.configs.security;

import com.shop_app.configs.security.jwt.JwtProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum TokenType {
    ACCESS(JwtProperties::access),
    REFRESH(JwtProperties::refresh);

    private final Function<JwtProperties, JwtProperties.TokenConfig> configMapper;
}
