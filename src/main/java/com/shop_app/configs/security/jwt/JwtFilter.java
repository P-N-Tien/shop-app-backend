package com.shop_app.configs.security.jwt;

import com.shop_app.configs.security.UserPrincipal;
import com.shop_app.configs.security.UserPrincipleDetailService;
import com.shop_app.configs.security.exceptions.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserPrincipleDetailService customUserDetailService;
    private final JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Extract access token from cookie
        String accessToken = resolveAccessToken(request);

        // 2. If there is no token, skip the filter
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 1. Validate token (signature, expiration)
            if (!jwtService.isTokenWellFormed(accessToken)) {
                SecurityContextHolder.clearContext();
                filterChain.doFilter(request, response);
                return;
            }

            String username = jwtService.extractUsername(accessToken);

            if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                // If user not found, CustomUserDetailService will throw exception
                UserPrincipal userPrincipal = (UserPrincipal)
                        customUserDetailService.loadUserByUsername(username);

                // 2. Validate token vs user (status, revoked, password changed)
                if (jwtService.isAccessTokenValid(accessToken, userPrincipal)) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userPrincipal,
                                    null,
                                    userPrincipal.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
            }

        } catch (JwtException ex) {
            log.warn("Invalid JWT: {}", ex.getMessage());
            SecurityContextHolder.clearContext();
        } catch (Exception ex) {
            log.error("Authentication error", ex);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private String resolveAccessToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        return Arrays.stream(request.getCookies())
                .filter(c -> jwtProperties.access().cookieName()
                        .equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
