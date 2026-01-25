package com.shop_app.order.response;

import com.shop_app.order.enums.OrderStatus;
import com.shop_app.order_details.entity.OrderDetail;
import com.shop_app.payment.enums.PaymentMethod;
import com.shop_app.shared.dto.BaseResponse;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderResponse extends BaseResponse {
    private String recipientName;
    private BigDecimal totalMoney;
    private String recipientPhone;
    private String recipientAddress;
    private String note;
    private PaymentMethod paymentMethod;
    private OrderStatus status;
    private List<OrderDetail> orderDetails = new ArrayList<>();
}
