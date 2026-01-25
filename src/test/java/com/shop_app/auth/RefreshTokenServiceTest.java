package com.shop_app.auth;

import com.shop_app.auth.refresh_token.RefreshToken;
import com.shop_app.auth.refresh_token.RefreshTokenRepository;
import com.shop_app.auth.refresh_token.RefreshTokenService;
import com.shop_app.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshTokenService Tests")
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    private UUID testJti;
    private long testUserId;
    private RefreshToken testToken;

    @BeforeEach
    void setUp() {
        testJti = UUID.randomUUID();
        testUserId = 1L;
        testToken = RefreshToken.builder()
                .jti(testJti)
                .userId(testUserId)
                .expiresAt(Instant.now().plusSeconds(3600))
                .revoked(false)
                .build();
    }

    // ==================== SAVE TESTS ====================

    @Test
    @DisplayName("Should save refresh token successfully")
    void save_Success() {
        // Given
        Duration expiration = Duration.ofHours(1);
        when(refreshTokenRepository.existsById(any(UUID.class))).thenReturn(false);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(testToken);

        // When
        assertThatCode(() -> refreshTokenService.save(testUserId, testJti, expiration))
                .doesNotThrowAnyException();

        // Then
        verify(refreshTokenRepository).save(argThat(token ->
                token.getJti().equals(testJti) &&
                        token.getUserId().equals(testUserId) &&
                        !token.isRevoked()
        ));
    }

    @Test
    @DisplayName("Should throw exception when saving duplicate JTI")
    void save_DuplicateJti_ThrowsException() {
        // Given
        Duration expiration = Duration.ofHours(1);
        when(refreshTokenRepository.existsById(testJti)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> refreshTokenService.save(testUserId, testJti, expiration))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    @DisplayName("Should throw exception when userId is invalid")
    void save_InvalidUserId_ThrowsException() {
        // When & Then
        assertThatThrownBy(() ->
                refreshTokenService.save(-1L, testJti, Duration.ofHours(1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User ID must be positive");
    }

    @Test
    @DisplayName("Should throw exception when JTI is null")
    void save_NullJti_ThrowsException() {
        // When & Then
        assertThatThrownBy(() ->
                refreshTokenService.save(testUserId, null, Duration.ofHours(1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("JTI cannot be null");
    }

    // ==================== EXISTS TESTS ====================

    @Test
    @DisplayName("Should return true when token exists and not revoked")
    void exists_TokenValid_ReturnsTrue() {
        // Given
        when(refreshTokenRepository.findById(testJti)).thenReturn(Optional.of(testToken));

        // When
        boolean result = refreshTokenService.exists(testJti);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when token is revoked")
    void exists_TokenRevoked_ReturnsFalse() {
        // Given
        testToken.setRevoked(true);
        when(refreshTokenRepository.findById(testJti)).thenReturn(Optional.of(testToken));

        // When
        boolean result = refreshTokenService.exists(testJti);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return false when token is expired")
    void exists_TokenExpired_ReturnsFalse() {
        // Given
        testToken.setExpiresAt(Instant.now().minusSeconds(3600));
        when(refreshTokenRepository.findById(testJti)).thenReturn(Optional.of(testToken));

        // When
        boolean result = refreshTokenService.exists(testJti);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return false when token not found")
    void exists_TokenNotFound_ReturnsFalse() {
        // Given
        when(refreshTokenRepository.findById(testJti)).thenReturn(Optional.empty());

        // When
        boolean result = refreshTokenService.exists(testJti);

        // Then
        assertThat(result).isFalse();
    }

    // ==================== REVOKE TESTS ====================

    @Test
    @DisplayName("Should revoke token successfully")
    void revoke_Success() {
        // Given
        when(refreshTokenRepository.findById(testJti)).thenReturn(Optional.of(testToken));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(testToken);

        // When
        assertThatCode(() -> refreshTokenService.revoke(testJti))
                .doesNotThrowAnyException();

        // Then
        verify(refreshTokenRepository).save(argThat(token ->
                token.isRevoked()
        ));
    }

    @Test
    @DisplayName("Should be idempotent when revoking already revoked token")
    void revoke_AlreadyRevoked_IsIdempotent() {
        // Given
        testToken.setRevoked(true);
        when(refreshTokenRepository.findById(testJti)).thenReturn(Optional.of(testToken));

        // When
        assertThatCode(() -> refreshTokenService.revoke(testJti))
                .doesNotThrowAnyException();

        // Then
        verify(refreshTokenRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when revoking non-existent token")
    void revoke_TokenNotFound_ThrowsException() {
        // Given
        when(refreshTokenRepository.findById(testJti)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> refreshTokenService.revoke(testJti))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("not found");
    }

    // ==================== CLEANUP TESTS ====================

//    @Test
//    @DisplayName("Should cleanup expired tokens")
//    void cleanupExpiredTokens_Success() {
//        // Given
//        int expectedDeletedCount = 5;
//        when(refreshTokenRepository.deleteExpiredAndRevoked(any(Instant.class)))
//                .thenReturn(expectedDeletedCount);
//
//        // When
//        int deletedCount = refreshTokenService.cleanupExpiredTokens();
//
//        // Then
//        assertThat(deletedCount).isEqualTo(expectedDeletedCount);
//        verify(refreshTokenRepository).deleteExpiredAndRevoked(any(Instant.class));
//    }

    // ==================== COUNT ACTIVE TOKENS TESTS ====================

//    @Test
//    @DisplayName("Should count active tokens for user")
//    void countActiveTokensByUserId_ReturnsCorrectCount() {
//        // Given
//        long expectedCount = 3L;
//        when(refreshTokenRepository.countByUserIdAndRevokedFalseAndExpiresAtAfter(
//                eq(testUserId), any(Instant.class)))
//                .thenReturn(expectedCount);
//
//        // When
//        long count = refreshTokenService.countActiveTokensByUserId(testUserId);
//
//        // Then
//        assertThat(count).isEqualTo(expectedCount);
//    }

    // ==================== HELPER METHODS ====================

    private RefreshToken createTestToken(UUID jti) {
        return RefreshToken.builder()
                .jti(jti)
                .userId(testUserId)
                .expiresAt(Instant.now().plusSeconds(3600))
                .revoked(false)
                .build();
    }
}
