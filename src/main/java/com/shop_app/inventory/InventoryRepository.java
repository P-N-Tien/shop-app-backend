package com.shop_app.inventory;

import com.shop_app.inventory.entity.Inventory;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    /**
     * SQL Atomic update
     * Database only update
     * <p>
     * - Not DB lock
     * - Not @Version
     * - Not Retry storm
     * - Check quantity
     * - Reduce quantity
     * - Not oversell
     * </p>
     */
    @Modifying
    @Query("""
                UPDATE Inventory s
                SET s.quantity = s.quantity - :qty,
                    s.reservedQuantity = s.reservedQuantity + :qty
                WHERE s.productId = :productId
                  AND s.quantity >= :qty
            """)
    int decreaseInventory(
            @Param("productId") long productId,
            @Param("qty") int qty
    );

    /**
     * Lock row
     *
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
                SELECT i
                FROM Inventory i
                WHERE i.productId = :productId
            """)
    @QueryHints({
            @QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000"),
            // Không lấy dữ liệu từ Cache
//            @QueryHint(name = "jakarta.persistence.cache.retrieveMode", value = "BYPASS"),
            // Cập nhật lại Cache bằng dữ liệu mới nhất từ DB
//            @QueryHint(name = "jakarta.persistence.cache.storeMode", value = "REFRESH")
    })
    Optional<Inventory> findByIdForUpdate(@Param("productId") long productId);

    Optional<Inventory> findByProductId(long productId);
}
