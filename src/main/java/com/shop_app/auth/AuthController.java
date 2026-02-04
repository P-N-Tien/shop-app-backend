package com.shop_app.auth;

import com.shop_app.auth.requests.UserLoginRequest;
import com.shop_app.auth.response.TokenPair;
import com.shop_app.configs.security.jwt.JwtCookieUtil;
import com.shop_app.user.request.UserRegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtCookieUtil jwtCookieUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegisterRequest req) {
        authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody @Valid UserLoginRequest req) {

        // 1. Perform login and create token pair
        TokenPair tokens = authService.login(req);

        // 2. Create access and refresh cookie pair
        String accessToken = jwtCookieUtil.addAccessTokenCookie(tokens.accessToken());
        String refreshToken = jwtCookieUtil.addRefreshTokenCookie(tokens.refreshToken());

        // 3. Set-Cookie in Response Header
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, accessToken)
                .header(HttpHeaders.SET_COOKIE, refreshToken)
                .body("Login successful");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @CookieValue(name = "refresh_token") String refreshToken
    ) {
        TokenPair tokens = authService.refreshToken(refreshToken);

        String accessTokenNew = jwtCookieUtil.addAccessTokenCookie(tokens.accessToken());
        String refreshTokenNew = jwtCookieUtil.addRefreshTokenCookie(tokens.refreshToken());

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, accessTokenNew)
                .header(HttpHeaders.SET_COOKIE, refreshTokenNew)
                .build();
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(name = "refresh_token") String refreshToken
    ) {
        authService.logout(refreshToken);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, jwtCookieUtil.removeAccessTokenCookie())
                .header(HttpHeaders.SET_COOKIE, jwtCookieUtil.removeRefreshTokenCookie())
                .body("Logout successful");
    }
}
