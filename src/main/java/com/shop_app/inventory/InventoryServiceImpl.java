package com.shop_app.inventory;

import com.shop_app.inventory.request.CreateInventoryRequest;
import com.shop_app.product.entity.Product;
import com.shop_app.product.validator.ProductValidator;
import com.shop_app.shared.exceptions.IllegalArgumentException;
import com.shop_app.shared.exceptions.NotFoundException;
import com.shop_app.inventory.entity.Inventory;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements IInventoryService {
    private final InventoryRepository repository;
    private final ProductValidator productValidator;

    @Override
    @Transactional
    public void createInventory(CreateInventoryRequest req) {
        Product product = productValidator.validateAndGet(req.getProductId());

        Inventory stock = Inventory.builder()
                .quantity(req.getQuantity())
                .product(product)
                .soldQuantity(0) // Default is 0
                .reservedQuantity(0) // Default is 0
                .build();

        repository.save(stock);
    }

    @Recover
    public void recover(OptimisticLockException ex, Long productId, int amount) {
        throw new IllegalArgumentException("Inventory is busy, please retry later");
    }

    /**
     * SELECT ... FOR UPDATE
     */
    public Inventory findByIdForUpdate(long productId) {
        return repository.findByIdForUpdate(productId)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Product not found with productId: " + productId));
    }
}
