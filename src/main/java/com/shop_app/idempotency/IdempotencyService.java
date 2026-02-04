package com.shop_app.idempotency;

public interface IdempotencyService {
    IdempotencyKey checkAndStartProcessing(String keyId);

    void markCompleted(String keyId, String responseBody);
}
