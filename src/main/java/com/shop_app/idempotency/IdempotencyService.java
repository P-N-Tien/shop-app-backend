package com.shop_app.idempotency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop_app.shared.exceptions.DuplicateException;
import com.shop_app.shared.exceptions.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class IdempotencyService implements IIdempotencyService {
    private final IdempotencyKeyRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public IdempotencyKey checkAndStartProcessing(String keyId) {
        // Using Lock (LockModeType.PESSIMISTIC_WRITE)
        // to prevent Race Condition (when 2 or more request at the same time)
        Optional<IdempotencyKey> existingKey = repository.findByKeyIdForUpdate(keyId);

        if (existingKey.isPresent()) {
            IdempotencyKey key = existingKey.get();

            if (key.getStatus() == IdempotencyKey.IdempotencyStatus.COMPLETED) {
                // Key was succeed -> return old value
                return key;
            } else if (key.getStatus() == IdempotencyKey.IdempotencyStatus.PENDING)
                throw new DuplicateException(
                        "Request with this key is already being processed.");
        }

        // Build new IdempotencyKey, if key not exists
        IdempotencyKey newKey = IdempotencyKey.builder()
                .keyId(keyId)
                .status(IdempotencyKey.IdempotencyStatus.PENDING)
                .build();

        log.info("[IDEMPOTENCY][CREATE] keyId={}", keyId);

        return repository.save(newKey);
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
