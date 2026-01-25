package com.shop_app.order;

import com.shop_app.order.request.CheckoutRequest;
import com.shop_app.order.response.CheckoutResponse;
import com.shop_app.order.response.OrderResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse getById(long id);

    List<OrderResponse> getOrderByUserId(long id);

    CheckoutResponse checkout(CheckoutRequest req);
}
