package com.shop_app.payment.method.vn_pay;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "vn-pay")
public record VnPayProperties(
        String apiUrl,
        String hashSecret,
        Map<String, String> fixedParams,
        FieldNames fieldNames,
        Config config
) {
    public record FieldNames(
            String amount,
            String txnRef,
            String orderInfo,
            String ipAddress,
            String createDate,
            String expireDate,
            String secureHash
    ) {
    }

    public record Config(int expireMinutes, String timezone) {
    }
}
