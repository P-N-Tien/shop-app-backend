package com.shop_app.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop_app.idempotency.IdempotencyService;
import com.shop_app.order.request.CheckoutRequest;
import com.shop_app.order.response.CheckoutResponse;
import com.shop_app.order.response.OrderResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;
    private final IdempotencyService idempotencyService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(
            @RequestBody @Valid CheckoutRequest req,
            HttpServletRequest request
    ) {
        String keyId = (String) request.getAttribute("idempotencyKeyId");

        // Execute business
        CheckoutResponse checkoutResponse = orderService.checkout(req);

        // 3. Update Idempotency Key (Only Key was sent)
        if (keyId != null) {
            try {
                // Convert Response Body to String to storage.
                ObjectMapper mapper = new ObjectMapper();
                String responseBody = mapper.writeValueAsString(null);

                idempotencyService.markCompleted(keyId, responseBody);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(checkoutResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getOrderByUser(@PathVariable long id) {
        return ResponseEntity.ok(orderService.getOrderByUserId(id));
    }
}
