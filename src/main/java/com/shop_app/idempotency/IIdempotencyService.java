package com.shop_app.idempotency;

public interface IIdempotencyService {
    IdempotencyKey checkAndStartProcessing(String keyId);

    void markCompleted(String keyId, String responseBody);
}
