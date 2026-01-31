package com.shop_app.order;

import com.shop_app.inventory.InventoryRepository;
import com.shop_app.inventory.entity.Inventory;
import com.shop_app.inventory.validator.InventoryValidator;
import com.shop_app.order.request.CheckoutRequest;
import com.shop_app.order.request.ItemRequest;
import com.shop_app.payment.enums.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OrderConcurrencyTest {

    private final IOrderService orderService;
    private final InventoryRepository inventoryRepository;
    private final InventoryValidator inventoryValidator;

    @Test
    void should_not_oversell_when_100_000_requests() throws Exception {

        //============= GIVEN ===============//
        long productId = 1L;
        int quantity = 1;
        int reservedQuantity = 0;
        long userId = 1L;
        int initialStock = 100;
        int numThreads = 1000;
        int numThreadPool = 100;

        // Initial Inventory: quantity = 10, reserved quantity = 0
        Inventory inventory = inventoryValidator.findByProductId(productId);
        inventory.setQuantity(initialStock);
        inventory.setReservedQuantity(reservedQuantity);
        inventoryRepository.saveAndFlush(inventory);

        System.out.println("Initial Inventory: " + inventory);

        // User buy product and quantity
        ItemRequest itemRequest1 = ItemRequest.builder()
                .productId(productId)
                .quantity(quantity)
                .build();

        // Client click order "button" and send data to server
        CheckoutRequest checkoutRequest = CheckoutRequest.builder()
                .recipientName("Tien")
                .recipientAddress("New York")
                .recipientPhone("092312312")
                .note("Hang de vo")
                .paymentMethod(PaymentMethod.VNPAY)
                .items(List.of(itemRequest1))
                .build();

        // Create 100 worker-threads run concurrency
        ExecutorService executorService = Executors.newFixedThreadPool(numThreadPool);
        CountDownLatch latch = new CountDownLatch(numThreads);

        AtomicInteger success = new AtomicInteger();
        AtomicInteger fail = new AtomicInteger();

        //============= WHEN ===============//
        for (int i = 0; i < numThreads; i++) {
            executorService.submit(() -> {
                try {
                    orderService.checkout(checkoutRequest);
                    success.incrementAndGet();
                } catch (Exception e) {
                    fail.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        //============= THEN ===============//
        int expectedQuantity = 0;
        int expectedReservedQuantity = 0;
        int expectedSuccess = 100;
        int expectedFail = 99900;

        Inventory inventoryFinal = inventoryValidator.findByProductId(productId);

        System.out.println("Final Inventory: " + inventoryFinal);

        System.out.println("Success: " + success.get());
        System.out.println("Fail: " + fail.get());
        System.out.println("Final Quantity: " + inventoryFinal.getQuantity());
        System.out.println("Final Reserved quantity: " +
                inventoryFinal.getReservedQuantity());

        assertEquals(expectedSuccess, success.get());
        assertEquals(expectedFail, fail.get());
        assertEquals(expectedQuantity, inventoryFinal.getQuantity());
        assertEquals(expectedReservedQuantity, inventoryFinal.getReservedQuantity());
    }
}