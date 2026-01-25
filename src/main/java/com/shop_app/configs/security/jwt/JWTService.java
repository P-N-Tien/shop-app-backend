package com.shop_app.configs.security.jwt;

import com.shop_app.auth.refresh_token.IRefreshTokenService;
import com.shop_app.configs.security.TokenType;
import com.shop_app.configs.security.UserPrincipal;
import com.shop_app.shared.validate.Validate;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;


@Slf4j
@Service
@RequiredArgsConstructor
@ConfigurationProperties(value = "jwt")
public class JWTService {

    private final IRefreshTokenService refreshTokenService;
    private final JwtProperties jwtProperties;

    /* ====================== KEY ====================== */

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtProperties.secret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /* ====================== GENERATE ====================== */

    public String generateAccessToken(UserPrincipal user) {
        Validate.requiredNonNull(user, "UserPrincipal is required");

        // We can add any claims
        Map<String, Object> claims = Map.of(
                "type", TokenType.ACCESS.name(),
                "userId", user.getUser().getId(),
                "roles", user.getAuthorities()
        );

        return buildToken(claims, user.getUsername(), jwtProperties.accessExpiration());
    }

    public String generateRefreshToken(UserPrincipal user) {
        Validate.requiredNonNull(user, "UserPrincipal is required");

        UUID jti = UUID.randomUUID();

        // 1. PRE-SAVE: Ensure JTI is logged in the system beforehand.
        // If a save error occurs, an Exception will be thrown and no incorrect token will be returned to the user.
        refreshTokenService.save(
                user.getUser().getId(),
                jti,
                jwtProperties.refreshExpiration()
        );

        // 2. CREATE LATER: Build a token based on the saved JTI
        Map<String, Object> claims = Map.of(
                "type", TokenType.REFRESH.name(),
                "jti", jti.toString()
        );

        return buildToken(claims, user.getUsername(), jwtProperties.refreshExpiration());
    }

    private String buildToken(Map<String, Object> extraClaims,
                              String subject, Duration duration) {
        Instant now = Instant.now();

        return Jwts.builder()
                .claims(extraClaims)
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(duration)))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    /* ====================== VALIDATE ====================== */

    public boolean isTokenWellFormed(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("Invalid JWT structure: {}", e.getMessage());
            return false;
        }
    }

    public boolean isAccessTokenValid(String token, UserPrincipal user) {
        Claims claims = parseClaims(token);

        return TokenType.ACCESS.name().equals(claims.get("type"))
                && claims.getSubject().equals(user.getUsername())
                && user.isEnabled()
                && !isExpired(claims);
    }

    public boolean isRefreshTokenValid(String token) {
        // Validate token
        if (!isTokenWellFormed(token)) {
            return false;
        }

        Claims claims = parseClaims(token);

        // Type inside Token (claims) must be "REFRESH"
        if (!TokenType.REFRESH.name().equals(claims.get("type"))) {
            return false;
        }

        // Get JTW ID from token
        UUID jti = extractJti(claims);

        return !isExpired(claims) && refreshTokenService.exists(jti);
    }

    private boolean isExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    /* ====================== EXTRACT ====================== */

    public String extractUsername(String token) {
        String subject = extractClaim(token, Claims::getSubject);
        return subject;
    }

    public UUID extractJti(Claims claims) {
        UUID jti = UUID.fromString((String) claims.get("jti"));
        return jti;
    }

    public UUID extractJtiToken(String token) {
        Claims claims = parseClaims(token);
        UUID jti = UUID.fromString((String) claims.get("jti"));
        return jti;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
