package com.shop_app.inventory.mapper;

import com.shop_app.inventory.entity.Inventory;
import com.shop_app.inventory.request.CreateInventoryRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    Inventory createFrom(CreateInventoryRequest req);

}
