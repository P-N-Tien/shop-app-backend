package com.shop_app.inventory.strategy;

public interface IReserveInventoryService {
    void reserve(long productId, int quantity);
}