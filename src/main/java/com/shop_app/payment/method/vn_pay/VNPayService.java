package com.shop_app.payment.method.vn_pay;

import com.shop_app.payment.entity.Payment;
import com.shop_app.payment.enums.PaymentStatus;
import com.shop_app.payment.exceptions.PaymentException;
import com.shop_app.payment.response.PaymentResponse;
import com.shop_app.shared.utils.NetworkUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class VNPayService {
    private static final DateTimeFormatter VNP_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final VnPayProperties vnPayProperties;
    private final VNPaySecurity vnPaySecurity;

    public PaymentResponse createPaymentUrl(Payment payment) {
        try {
            // 1. Use TreeMap to automatically sort parameter names
            // in ascending order.
            Map<String, String> vnpParams = new TreeMap<>(
                    // Set default value from fixed params in VNPay config (yml)
                    vnPayProperties.fixedParams());

            // Get params from config
            VnPayProperties.FieldNames fieldNames = vnPayProperties.fieldNames();
            VnPayProperties.Config config = vnPayProperties.config();

            // Calculate the amount (multiply by 100 as per VNPay regulations).
            BigDecimal amount = payment.getAmount();

            if (amount == null || amount.signum() <= 0) {
                throw new IllegalArgumentException("Invalid payment amount");
            }

            // Remove the decimal part and multiply by 100
            vnpParams.put(
                    fieldNames.amount(),
                    amount.multiply(BigDecimal.valueOf(100))
                            .setScale(0, RoundingMode.DOWN)
                            .toPlainString()
            );

            vnpParams.put(fieldNames.txnRef(), payment.getTransactionId());
            vnpParams.put(fieldNames.orderInfo(),
                    "Thanh toan don hang: " + payment.getOrder().getId());
            vnpParams.put(fieldNames.ipAddress(), NetworkUtils.getClientIp());

            ZonedDateTime now = ZonedDateTime.now(ZoneId.of(config.timezone()));
            String createDate = now.format(VNP_DATE_FORMATTER);
            vnpParams.put(fieldNames.createDate(), createDate);

            ZonedDateTime expireDate = now.plusMinutes(config.expireMinutes());
            vnpParams.put(fieldNames.expireDate(),
                    expireDate.format(VNP_DATE_FORMATTER));

            // 2. Build hash data
            String hashData = vnPaySecurity.buildHashData(vnpParams);
            String vnpSecureHash = vnPaySecurity.hmacSHA512(
                    vnPayProperties.hashSecret(), hashData);

            vnpParams.put(fieldNames.secureHash(), vnpSecureHash);

            // 3. Build query string
            String queryUrl = buildQueryString(vnpParams);

            // 4. Redirect
            return PaymentResponse.builder()
                    .status(PaymentStatus.PENDING)
                    .message("Redirect to VNPay to payment")
                    .paymentUrl(vnPayProperties.apiUrl() + "?" + queryUrl)
                    .build();

        } catch (RuntimeException e) {
            log.error("[VNPAY] Error creating payment URL, paymentId={}",
                    payment.getId(), e);
            throw new PaymentException("Cannot create VNPay payment URL");
        }
    }

    /**
     * Request from VNPay: sort parameter names in ascending order
     */
    private String buildQueryString(Map<String, String> params) {
        return params.entrySet().stream()
                .filter(e -> e.getValue() != null && !e.getValue().isEmpty())
                .sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    try {
                        return e.getKey() + "=" +
                                URLEncoder.encode(e.getValue(),
                                        StandardCharsets.US_ASCII.toString()
                                );
                    } catch (UnsupportedEncodingException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .collect(Collectors.joining("&"));
    }
}