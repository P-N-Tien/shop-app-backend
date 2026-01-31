package com.shop_app.payment.method.vn_pay;

import java.util.Map;

public interface VnpayService {
    VNPayIPNResponse processIPN(Map<String, String> params);
}
