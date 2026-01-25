package com.shop_app.order.mapper;

import com.shop_app.order.request.CheckoutRequest;
import com.shop_app.order.entity.Order;
import com.shop_app.order.response.OrderResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "userId", ignore = true)
    Order toEntity(CheckoutRequest req);

    OrderResponse toResponse(Order entity);
}