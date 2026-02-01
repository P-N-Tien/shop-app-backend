package com.shop_app.payment;

import com.shop_app.payment.method.vn_pay.response.VNPayIPNResponse;
import com.shop_app.payment.method.vn_pay.VnpayService;
import com.shop_app.payment.method.vn_pay.response.VNPayValidateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final VnpayService vnpayService;

    @RequestMapping(value = "/IPN", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<VNPayIPNResponse> vnpayIPN(@RequestParam Map<String, String> allParams) {
        log.info("VNPAY IPN Request: {}", allParams);
        var response = vnpayService.processIPN(allParams);
        log.info("VNPAY IPN Response: {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/vnpay/validate")
    public ResponseEntity<VNPayValidateResponse> validatePayment(@RequestParam Map<String, String> allParams) {
        return ResponseEntity.ok(vnpayService.validatePayment(allParams));
    }
}
