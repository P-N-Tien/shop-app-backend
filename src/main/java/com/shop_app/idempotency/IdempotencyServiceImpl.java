package com.shop_app.idempotency;

import com.shop_app.configs.interceptor.DuplicateRequestException;
import com.shop_app.shared.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdempotencyServiceImpl implements IdempotencyService {
    private final IdempotencyKeyRepository repository;

    @Override
    @Transactional
    public IdempotencyKey checkAndStartProcessing(String keyId) {
        // Using Lock (LockModeType.PESSIMISTIC_WRITE)
        // to prevent Race Condition (when 2 or more request at the same time)
        return repository.findByKeyIdForUpdate(keyId)
                .map(key -> {
                    if (key.getStatus() == IdempotencyKey.IdempotencyStatus.COMPLETED)
                        return key;
                    throw new DuplicateRequestException("Processing in progress");
                })
                .orElseGet(() -> {
                    IdempotencyKey newKey = IdempotencyKey.builder()
                            .keyId(keyId)
                            .status(IdempotencyKey.IdempotencyStatus.PENDING)
                            .build();
                    return repository.save(newKey);
                });
    }

    /**
     * Update Key when process successfully.
     */
    @Override
    @Transactional
    public void markCompleted(String keyId, String responseBody) {
        IdempotencyKey key = repository.findByKeyId(keyId)
                .orElseThrow(() ->
                        new NotFoundException("Idempotency Key not found."));

        key.setStatus(IdempotencyKey.IdempotencyStatus.COMPLETED);
        key.setResponseBody(responseBody);

        log.info("[IDEMPOTENCY][MARK_COMPLETE] keyId={}", keyId);
    }
}
