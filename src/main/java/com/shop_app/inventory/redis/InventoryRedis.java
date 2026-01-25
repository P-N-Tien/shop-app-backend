package com.shop_app.inventory.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Configuration
public class InventoryRedis {
    @Bean
    public DefaultRedisScript<Long> inventoryDecreaseScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();

        // 1. Define Lua Script
        redisScript.setScriptText(
                new StringBuilder()
                        .append("local key = KEYS[1]\n")
                        .append("local qty = tonumber(ARGV[1])\n")
                        .append("local current = tonumber(redis.call('GET', key) or '0')\n")
                        .append("if current <= 0 then return -1 end\n")
                        .append("if current < qty then return -1 end\n")
                        .append("redis.call('DECRBY', key, qty)\n")
                        .append("return current - qty")
                        .toString()
        );
        // 2. Define the type to return
        redisScript.setResultType(Long.class);

        return redisScript;
    }

    @Bean
    public DefaultRedisScript<Long> inventoryIncreaseScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();

        // 1. Define Lua Script
        redisScript.setScriptText(
                new StringBuilder()
                        .append("local key = KEYS[1]\n")
                        .append("local qty = tonumber(ARGV[1])\n")
                        .append("if qty <= 0 or not qty then return 0 end\n")
                        .append("local new_current = redis.call('INCRBY', key, qty)\n")
                        .append("return new_current")
                        .toString()
        );

        // 2. Define the type to return
        redisScript.setResultType(Long.class);

        return redisScript;
    }
}
