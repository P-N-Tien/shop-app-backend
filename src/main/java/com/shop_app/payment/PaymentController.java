package com.shop_app.payment;

import com.shop_app.payment.method.vn_pay.VNPayIPNResponse;
import com.shop_app.payment.method.vn_pay.VNPayUtil;
import com.shop_app.payment.method.vn_pay.VnpayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/IPN")
public class PaymentController {

    private final VnpayService vnpayService;

    @GetMapping
    public ResponseEntity<VNPayIPNResponse> vnpayIPN(@RequestParam Map<String, String> allParams) {
        log.info("VNPAY IPN Request: {}", allParams);
        var response = vnpayService.processIPN(allParams);
        log.info("VNPAY IPN Response: {}", response);
        return ResponseEntity.ok(response);
    }
}
