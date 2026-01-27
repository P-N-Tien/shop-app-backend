package com.shop_app.product.redis;

import com.shop_app.order.exceptions.OutOfInventoryException;
import com.shop_app.order.request.ItemRequest;
import com.shop_app.shared.validate.Validate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
//@RequiredArgsConstructor
public class ProductRedis {

//    private final RedisTemplate<String, Long> redisTemplate;
//    private final DefaultRedisScript<Long> inventoryDecreaseScript;
//
//    public void decreaseRedisStock(ItemRequest item) {
//        Validate.requiredNonNull(item, "ItemRequest must be not null");
//
//        String stockKey = "stock:" + item.getProductId();
//
//        Long result = redisTemplate.execute(
//                inventoryDecreaseScript,
//                List.of(stockKey),
//                item.getQuantity()
//        );
//
//        if (result == null || result < 0) {
//            throw new OutOfInventoryException("Out of inventory");
//        }
//    }
}
