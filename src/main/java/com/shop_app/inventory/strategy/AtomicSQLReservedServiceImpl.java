package com.shop_app.inventory.strategy;

import com.shop_app.inventory.InventoryRepository;
import com.shop_app.order.exceptions.OutOfInventoryException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AtomicSQLReservedServiceImpl implements IReserveInventoryService {
    private final InventoryRepository repository;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void reserve(long productId, int quantity) {
        int updatedRows = repository.decreaseInventory(
                productId,
                quantity
        );

        // If the update fails → out of stock
        if (updatedRows == 0)
            throw new OutOfInventoryException(
                    "Out of inventory for productId: " + productId);
    }
}
