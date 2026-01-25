package com.shop_app.payment.method.vn_pay;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class VNPaySecurity {

    private final VnPayProperties vnPayProperties;

    public String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(
                    key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] result = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : result) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error generating HMAC SHA512", e);
        }
    }

    public boolean verifySignature(Map<String, String> params) {
        try {
            String vnpSecureHash = params.get(
                    vnPayProperties.fieldNames().secureHash()
            );
            params.remove(vnPayProperties.fieldNames().secureHash());

            String hashData = buildHashData(params);
            String calculatedHash = hmacSHA512(
                    vnPayProperties.hashSecret(), hashData);

            return vnpSecureHash.equals(calculatedHash);
        } catch (Exception e) {
            log.error("[VNPAY] Error verifying signature", e);
            return false;
        }
    }

    public String buildHashData(Map<String, String> params) {
        return params.entrySet().stream()
                .filter(e -> e.getValue() != null && !e.getValue().isEmpty())
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));
    }
}
