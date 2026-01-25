package com.shop_app.inventory.strategy;

import com.shop_app.order.exceptions.InsufficientInventoryException;
import com.shop_app.inventory.InventoryRepository;
import com.shop_app.inventory.entity.Inventory;
import com.shop_app.inventory.validator.InventoryValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OptimisticReserveServiceImpl implements IReserveInventoryService {
    private final InventoryValidator inventoryValidator;
    private final InventoryRepository inventoryRepository;

    @Override
    @Retryable(
            value = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 100, multiplier = 2)
    )
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void reserve(long productId, int quantity) {
        updateReserve(productId, quantity);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    private void updateReserve(long productId, int quantity) {
        Inventory inventory = inventoryValidator.findByProductId(productId);

        // Check that there is sufficient inventory
        if (inventory.getQuantity() < quantity)
            throw new InsufficientInventoryException(
                    "Out of inventory for productId: " + productId);

        // decrease quantity and increase reserved quantity
        inventory.reserve(quantity);

        inventoryRepository.saveAndFlush(inventory); // Now
    }
}
