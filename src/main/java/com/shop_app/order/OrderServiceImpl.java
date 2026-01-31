package com.shop_app.order;

import com.shop_app.order.enums.OrderStatus;
import com.shop_app.order.exceptions.ProductNotAvailableException;
import com.shop_app.order.mapper.OrderMapper;
import com.shop_app.order.request.CheckoutRequest;
import com.shop_app.order.request.ItemRequest;
import com.shop_app.order.entity.Order;
import com.shop_app.order.response.CheckoutResponse;
import com.shop_app.order.response.OrderResponse;
import com.shop_app.order.validator.OrderValidator;
import com.shop_app.order_details.entity.OrderDetail;
import com.shop_app.payment.PaymentService;
import com.shop_app.payment.entity.Payment;
import com.shop_app.payment.response.PaymentResponse;
import com.shop_app.product.entity.Product;
import com.shop_app.product.enums.ProductStatus;
import com.shop_app.inventory.strategy.IReserveInventoryService;
import com.shop_app.product.validator.ProductValidator;
import com.shop_app.shared.utils.SecurityUtils;
import com.shop_app.shared.validate.Validate;
import com.shop_app.user.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final PaymentService paymentService;
    private final UserValidator userValidator;
    private final ProductValidator productValidator;
    private final OrderValidator orderValidator;
    //    private final ProductRedis productRedis;
    private final IReserveInventoryService reserveStockService;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public CheckoutResponse checkout(CheckoutRequest req) {
        // Validate input
        Validate.requiredNonNull(req, "Order must be not null");

        // 1. Mapping user shipping info
        Order order = orderMapper.toEntity(req);
        BigDecimal totalSum = BigDecimal.ZERO;

        // 2. For each item: validate product, lock stock, reserve
        for (ItemRequest item : req.items()) {

            // 3. Atomic Decrease stock in REDIS
//            productRedis.decreaseRedisStock(item);

            Product product = productValidator.validateAndGet(item.getProductId());

            // validate the product and still sell
            if (product == null || product.getStatus() != ProductStatus.ACTIVE)
                throw new ProductNotAvailableException(
                        "Product is not available with id: " + product.getId());

            // 4. Decrease quantity and increase reserved-quantity
            reserveStockService.reserve(
                    item.getProductId(),
                    item.getQuantity()
            );

            // 5. Build Order Detail
            OrderDetail orderDetail = OrderDetail.builder()
                    .product(product)
                    .name(product.getName())
                    .priceAtPurchase(product.getPrice()) // Get price from db
                    .quantity(item.getQuantity())
                    .build();

            order.addOrderDetail(orderDetail);
            totalSum = totalSum.add(orderDetail.getTotalPrice());
        }

        // 6. Complete order
        order.setUser(userValidator.validateAndGet(SecurityUtils.getCurrentUserId()));
        order.setStatus(OrderStatus.PENDING);
        order.setTotalMoney(totalSum);

        Order savedOrder = orderRepository.save(order);

        log.info("[ORDER][CREATE] Create Order successful with id={} userId={}",
                savedOrder.getId(), savedOrder.getUserId());

        // Create a payment record (VNPAY) with PENDING
        Payment payment = paymentService.createPayment(savedOrder);
        // Create a link to redirect the user to VNPay Web
        PaymentResponse paymentResponse = paymentService.processPayment(
                order.getPaymentMethod(), payment
        );

        return new CheckoutResponse(
                order.getId(),
                req.paymentMethod(),
                paymentResponse.status(),
                paymentResponse.url(),
                paymentResponse.message()
        );
    }

    @Override
    public OrderResponse getById(long id) {
        return orderMapper.toResponse(orderValidator.validateAndGet(id));
    }

    @Override
    public List<OrderResponse> getOrderByUserId(long id) {
        return orderRepository.findByUserId(id).stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }
}
