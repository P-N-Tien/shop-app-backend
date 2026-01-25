package com.shop_app.inventory.validator;

import com.shop_app.shared.exceptions.NotFoundException;
import com.shop_app.inventory.InventoryRepository;
import com.shop_app.inventory.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryValidator {
    private final InventoryRepository inventoryRepository;

    public Inventory findByProductId(long productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Product not found with productId: " + productId));
    }

}
