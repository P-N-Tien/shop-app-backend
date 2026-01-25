package com.shop_app.auth.refresh_token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupScheduler {

    private final IRefreshTokenService refreshTokenService;

    @Transactional
    @Scheduled(cron = "${app.scheduling.cleanup-cron}")
    public void cleanupExpiredTokens() {
        log.info("[REFRESH_TOKEN][CLEAN_UP] Starting scheduled cleanup of expired refresh tokens");
        try {
            int deletedCount = refreshTokenService.cleanupExpiredTokens();
            log.info("[REFRESH_TOKEN][COMPLETE] Completed scheduled cleanup. Deleted {} expired tokens", deletedCount);
        } catch (Exception e) {
            log.error("[REFRESH_TOKEN][ERROR] Error during scheduled token cleanup", e);
        }
    }

    //    @Scheduled(fixedDelay = 21600000)
    public void cleanupExpiredTokensFrequent() {
        cleanupExpiredTokens();
    }
}