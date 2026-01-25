package com.shop_app.inventory.strategy;

import com.shop_app.inventory.IInventoryService;
import com.shop_app.inventory.InventoryRepository;
import com.shop_app.inventory.entity.Inventory;
import com.shop_app.order.exceptions.OutOfInventoryException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PessimisticReservedServiceImpl implements IReserveInventoryService {
    private final IInventoryService inventoryService;
    private final InventoryRepository repository;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void reserve(long productId, int quantity) {
        try {
            Inventory inventory = inventoryService.findByIdForUpdate(productId);

            if (inventory.getQuantity() < quantity)
                throw new OutOfInventoryException(
                        "Out of inventory for productId: " + productId);

            // Decrease quantity and increase reserved-quantity
            inventory.reserve(quantity);

            Thread.sleep(1000);
            repository.saveAndFlush(inventory); // Now
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
    }
}
