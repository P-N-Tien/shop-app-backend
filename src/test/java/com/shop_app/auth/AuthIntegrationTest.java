package com.shop_app.auth;

import com.shop_app.auth.requests.UserLoginRequest;
import com.shop_app.auth.response.TokenPair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Authentication Integration Tests")
class AuthIntegrationTest {

    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("Should complete full authentication flow")
    void fullAuthenticationFlow() {
        // 1. Login
        UserLoginRequest loginRequest = new UserLoginRequest(
                "00000000001",
                "123456"
        );

        TokenPair tokens = authService.login(loginRequest);

        assertThat(tokens).isNotNull();
        assertThat(tokens.accessToken()).isNotBlank();
        assertThat(tokens.refreshToken()).isNotBlank();

        // 2. Refresh token
        TokenPair newTokens = authService.refreshToken(tokens.refreshToken());

        assertThat(newTokens).isNotNull();

        assertThat(newTokens.accessToken()).isNotEqualTo(tokens.accessToken());
        assertThat(newTokens.refreshToken()).isNotEqualTo(tokens.refreshToken());

        // 3. Logout
        assertThatCode(() -> authService.logout(newTokens.refreshToken()))
                .doesNotThrowAnyException();
    }
}