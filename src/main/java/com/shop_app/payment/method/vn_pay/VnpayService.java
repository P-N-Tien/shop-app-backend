package com.shop_app.payment.method.vn_pay;

import com.shop_app.payment.method.vn_pay.response.VNPayIPNResponse;
import com.shop_app.payment.method.vn_pay.response.VNPayValidateResponse;

import java.util.Map;

public interface VnpayService {
    VNPayValidateResponse validatePayment(Map<String, String> queryParams);

    VNPayIPNResponse processIPN(Map<String, String> params);
}
