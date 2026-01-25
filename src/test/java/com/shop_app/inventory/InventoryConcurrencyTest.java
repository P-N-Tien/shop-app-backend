package com.shop_app.inventory;

import com.shop_app.inventory.entity.Inventory;
import com.shop_app.inventory.strategy.IReserveInventoryService;
import com.shop_app.inventory.validator.InventoryValidator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class InventoryConcurrencyTest {

    private final InventoryRepository inventoryRepository;
    private final IReserveInventoryService reserveInventoryService;
    private final InventoryValidator inventoryValidator;

    @Test
    void should_not_oversell_when_10_concurrency_requests()
            throws InterruptedException {

        //============= GIVEN ===============//

        long productId = 1L;
        int reservedQuantity = 0;
        int initialProductQuantity = 2; // 10 product in inventory
        int numOfThreads = 2; // 10 people buy at the same time
        int quantity = 1; // the quantity that each user buy

        // 1. Init data: Inventory(quantity = 10, reserved quantity = 0)
        Inventory inventory = inventoryValidator.findByProductId(productId);
        inventory.setQuantity(initialProductQuantity);
        inventory.setReservedQuantity(reservedQuantity);
        inventoryRepository.saveAndFlush(inventory); // Now

        // 2. Simulate concurrency
        ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
        CountDownLatch latch = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(numOfThreads); // wait finish

        //============= WHEN ===============//

        // 10 people checkout at the same time
        for (int i = 0; i < numOfThreads; i++) {
            executor.execute(() -> {
                try {
                    latch.await(); // Wait here
                    System.out.println("RUNNER: "
                            + Thread.currentThread().getName());
                    reserveInventoryService.reserve(productId, quantity);
                    System.out.println("SUCCESS: "
                            + Thread.currentThread().getName());
                } catch (Exception e) {
                    System.err.println("ERROR: "
                            + Thread.currentThread().getName() + ", " + e.getMessage());
                } finally {
                    doneSignal.countDown();
                }
            });
        }

        // 3. Activate all threads simultaneously
        latch.countDown();
        doneSignal.await(); // Wait until all 10 threads have finished their work

        //============= THEN ===============//

        int expectedQuantity = 0;
        int expectedReservedQuantity = 1;

        Inventory finalInventory = inventoryValidator.findByProductId(productId);

        System.out.println("Final quantity: " + finalInventory.getQuantity());
        System.out.println("Final reversed-quantity: " + finalInventory.getReservedQuantity());

        // 4. Check the final result
        assertEquals(expectedQuantity, finalInventory.getQuantity());
        assertEquals(expectedReservedQuantity, finalInventory.getReservedQuantity());
    }
}