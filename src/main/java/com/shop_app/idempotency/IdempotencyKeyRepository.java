package com.shop_app.idempotency;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IdempotencyKeyRepository
        extends JpaRepository<IdempotencyKey, Long> {

    /**
     * Find Idempotency Key by keyId and apply Pessimistic Write Lock
     *
     * @param keyId Value of Idempotency-Key from Header
     * @return Optional Idempotency if exists
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT idem FROM IdempotencyKey idem WHERE idem.keyId = :keyId")
    Optional<IdempotencyKey> findByKeyIdForUpdate(@Param("keyId") String keyId);

    Optional<IdempotencyKey> findByKeyId(String keyId);
}
