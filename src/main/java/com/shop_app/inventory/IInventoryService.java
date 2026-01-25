package com.shop_app.inventory;

import com.shop_app.inventory.entity.Inventory;
import com.shop_app.inventory.request.CreateInventoryRequest;

public interface IInventoryService {
    Inventory findByIdForUpdate(long id);

    void createInventory(CreateInventoryRequest req);
}
