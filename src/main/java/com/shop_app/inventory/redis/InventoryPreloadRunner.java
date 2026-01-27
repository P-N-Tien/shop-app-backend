package com.shop_app.inventory.redis;

import com.shop_app.inventory.InventoryRepository;
import com.shop_app.inventory.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
//@RequiredArgsConstructor
public class InventoryPreloadRunner
//        implements ApplicationRunner

{

//    private final InventoryRepository inventoryRepository;
//    private final RedisTemplate<String, Long> redisTemplate;

//    @Override
//    public void run(ApplicationArguments args) {
//        List<Inventory> inventories = inventoryRepository.findAll();
//
//        for (Inventory s : inventories) {
//            redisTemplate.opsForValue()
//                    .set("stock:" + s.getProductId(), (long) s.getQuantity());
//        }
//    }
}