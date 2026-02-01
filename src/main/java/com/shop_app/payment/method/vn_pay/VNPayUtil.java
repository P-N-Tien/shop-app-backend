package com.shop_app.payment.method.vn_pay;

import com.shop_app.shared.validate.Validate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class VNPayUtil {

    private final VnPayProperties vnPayProperties;

    public static String hmacSHA512(final String key, final String data) {
        try {

            if (key == null || data == null) {
                log.error("[VNPAY] Key or data is null");
                throw new NullPointerException();
            }

            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            log.error("[VNPAY] Error : {} ", ex.getMessage());
            return "";
        }
    }

    public String hashAllFields(Map fields) {
        List fieldNames = new ArrayList(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder sb = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                sb.append(fieldName);
                sb.append("=");
                sb.append(fieldValue);
            }
            if (itr.hasNext()) {
                sb.append("&");
            }
        }
        return hmacSHA512(vnPayProperties.hashSecret(), sb.toString());
    }

    public boolean verifySignature(@NonNull Map<String, String> fields, String secureHash) {
        String rawData = fields.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .filter(entry -> entry.getValue() != null
                        && !entry.getValue().isEmpty())
                .map(entry -> entry.getKey() + "=" +
                        URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))
                .collect(Collectors.joining("&"));

        String generatedHash = hmacSHA512(vnPayProperties.hashSecret(), rawData);
        return generatedHash.equalsIgnoreCase(secureHash);
    }

    public String buildHashData(Map<String, String> params) {
        return params.entrySet().stream()
                .filter(e -> e.getValue() != null && !e.getValue().isEmpty())
                .filter(e -> !e.getKey().equals("vnp_SecureHash"))
                .filter(e -> !e.getKey().equals("vnp_SecureHashType"))
                .sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    try {
                        return e.getKey() + "=" +
                                URLEncoder.encode(e.getValue(),
                                        StandardCharsets.US_ASCII.toString());
                    } catch (UnsupportedEncodingException ex) {
                        log.error("[VNPAY] Error build hash data: {}", ex.getMessage());
                        throw new RuntimeException(ex);
                    }
                })
                .collect(Collectors.joining("&"));
    }

    public String buildQueryString(Map<String, String> params) {
        return params.entrySet().stream()
                .filter(e -> e.getValue() != null && !e.getValue().isEmpty())
                .sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    try {
                        return e.getKey() + "=" +
                                URLEncoder.encode(e.getValue(),
                                        StandardCharsets.US_ASCII.toString());
                    } catch (UnsupportedEncodingException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .collect(Collectors.joining("&"));
    }
}
