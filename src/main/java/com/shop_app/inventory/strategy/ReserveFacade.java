package com.shop_app.inventory.strategy;

import com.shop_app.inventory.InventoryRepository;
import com.shop_app.inventory.entity.Inventory;
import com.shop_app.inventory.validator.InventoryValidator;
import com.shop_app.order.exceptions.InsufficientInventoryException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReserveFacade {
    private final InventoryValidator inventoryValidator;
    private final InventoryRepository inventoryRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void updateReserve(long productId, int quantity) {
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