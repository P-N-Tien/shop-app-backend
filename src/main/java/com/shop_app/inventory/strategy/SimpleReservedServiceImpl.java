package com.shop_app.inventory.strategy;


import com.shop_app.inventory.InventoryRepository;
import com.shop_app.inventory.entity.Inventory;
import com.shop_app.inventory.validator.InventoryValidator;
import com.shop_app.order.exceptions.OutOfInventoryException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Primary
public class SimpleReservedServiceImpl implements IReserveInventoryService {
    private final InventoryRepository repository;
    private final InventoryValidator inventoryValidator;

    /**
     * This case can happen: Lost update
     * <p>
     * If 2 user checkout out at the same time (concurrency):
     * Quantity in inventory = 1, but
     * Transaction (user-1): read quantity = 1 -> update quantity = 0
     * Transaction (user-2): read quantity = 1 -> update quantity = 0
     * </p>
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void reserve(long productId, int quantity) {

        Inventory inventory = inventoryValidator.findByProductId(productId);

        if (inventory.getQuantity() < quantity)
            throw new OutOfInventoryException(
                    "Out of inventory for productId: " + productId);

        // Decrease quantity and increase reserved-quantity
        inventory.reserve(quantity);

        // Now
        repository.saveAndFlush(inventory);
    }
}
