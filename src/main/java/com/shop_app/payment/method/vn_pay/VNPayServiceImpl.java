package com.shop_app.payment.method.vn_pay;

import com.shop_app.order.OrderRepository;
import com.shop_app.order.entity.Order;
import com.shop_app.order.enums.OrderStatus;
import com.shop_app.payment.PaymentRepository;
import com.shop_app.payment.entity.Payment;
import com.shop_app.payment.enums.PaymentStatus;
import com.shop_app.payment.exceptions.PaymentException;
import com.shop_app.payment.method.vn_pay.response.VNPayIPNResponse;
import com.shop_app.payment.method.vn_pay.response.VNPayValidateResponse;
import com.shop_app.payment.response.PaymentResponse;
import com.shop_app.shared.utils.NetworkUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class VNPayServiceImpl implements VnpayService {
    private static final String VNP_DATE_FORMATTER = "yyyyMMddHHmmss";
    private final PaymentRepository paymentRepository;
    private final VnPayProperties vnPayProperties;
    private final VNPayUtil vnPayUtil;
    private final OrderRepository orderRepository;

    public PaymentResponse createPaymentUrl(Payment payment) {
        try {
            // 1. Build the params from VNPAY config
            Map<String, String> vnpParams = new TreeMap<>(vnPayProperties.fixedParams());

            // Get params from config
            VnPayProperties.FieldNames fieldNames = vnPayProperties.fieldNames();
            VnPayProperties.Config config = vnPayProperties.config();

            // Calculate the amount (multiply by 100 as per VNPay regulations).
            BigDecimal amount = payment.getAmount();

            if (amount == null || amount.signum() <= 0) {
                throw new IllegalArgumentException("Invalid payment amount");
            }

            vnpParams.put(
                    fieldNames.amount(),
                    amount.multiply(BigDecimal.valueOf(100))
                            .setScale(0, RoundingMode.DOWN)
                            .toPlainString()
            );

            vnpParams.put(fieldNames.txnRef(), payment.getTransactionId());
            vnpParams.put(fieldNames.orderInfo(),
                    "Thanh toan hoa don: " + payment.getOrder().getId());

            // Ensure that NetworkUtils processes Proxy/LoadBalancer correctly
            vnpParams.put(fieldNames.ipAddress(), NetworkUtils.getClientIp());

            // Timezone: Etc/GMT+7
            // Get TimeZone from config
            TimeZone tz = TimeZone.getTimeZone(config.timezone());

            // Initial Calendar with this TimeZone
            Calendar calendar = Calendar.getInstance(tz);
            SimpleDateFormat formatter = new SimpleDateFormat(VNP_DATE_FORMATTER);

            // Must set TimeZone for formatter
            formatter.setTimeZone(tz);

            String createDate = formatter.format(calendar.getTime());
            vnpParams.put(fieldNames.createDate(), createDate);

            // Add 15 minutes
            calendar.add(Calendar.MINUTE, config.expireMinutes());
            String expireDate = formatter.format(calendar.getTime());
            vnpParams.put(fieldNames.expireDate(), expireDate);

            // 2. Build hash data
            String hashData = vnPayUtil.buildHashData(vnpParams);

            // 3. Create the hmacSHA512 signiture
            String vnpSecureHash = vnPayUtil.hmacSHA512(
                    vnPayProperties.hashSecret(), hashData);

            // 4. Create the final Query String ( with Hash at the end)
            String queryUrl = vnPayUtil.buildQueryString(vnpParams) + "&"
                    + vnPayProperties.fieldNames().secureHash() + "=" + vnpSecureHash;

            // 5. Payment url
            String paymentUrl = vnPayProperties.apiUrl() + "?" + queryUrl;

            log.info("[VNPAY] Create payment successfully with " +
                    "paymentId={}, paymentUrl={}", payment.getId(), paymentUrl);

            // 6. Redirect
            return PaymentResponse.builder()
                    .status(PaymentStatus.PENDING)
                    .message("Redirect to VNPay to payment")
                    .url(paymentUrl)
                    .build();

        } catch (Exception e) {
            log.error("[VNPAY] Error creating payment URL, paymentId={}: {}",
                    payment.getId(), e.getMessage());
            throw new PaymentException("Cannot create VNPay payment URL" + e);
        }
    }

    @Override
    public VNPayValidateResponse validatePayment(Map<String, String> params) {
        try {
            String vnpSecureHash = params.get("vnp_SecureHash");
            Map<String, String> fields = new HashMap<>(params);
            fields.remove("vnp_SecureHashType");
            fields.remove("vnp_SecureHash");

            if (!vnPayUtil.verifySignature(fields, vnpSecureHash)) {
                return new VNPayValidateResponse(false, "Invalid security signature", PaymentStatus.FAILED);
            }

            String txnRef = params.get("vnp_TxnRef");
            long vnpAmount = Long.parseLong(params.get("vnp_Amount"));
            String responseCode = params.get("vnp_ResponseCode");

            // 2. Check order has exists (code: 01)
            if (!"00".equals(responseCode)) {
                return new VNPayValidateResponse(true, "Transaction failed or cancelled by user", PaymentStatus.FAILED);
            }

            // 3. Find transaction in DB
            return paymentRepository.findByTransactionId(txnRef)
                    .map(payment -> {
                        Order order = payment.getOrder();

                        // Validate order money is the same in db
                        long dbAmountX100 = order.getTotalMoney()
                                .multiply(new BigDecimal(100))
                                .longValue();

                        if (dbAmountX100 != vnpAmount || !(payment.getTransactionId().equals(txnRef))) {
                            return new VNPayValidateResponse(true, "Amount mismatch", PaymentStatus.FAILED);
                        }

                        return switch (order.getStatus()) {
                            case PAID -> new VNPayValidateResponse(true, "Payment successful", PaymentStatus.SUCCESS);
                            case CANCELLED, FAILED ->
                                    new VNPayValidateResponse(true, "Payment failed", PaymentStatus.FAILED);
                            case PENDING ->
                                    new VNPayValidateResponse(true, "Waiting for confirmation", PaymentStatus.PENDING);
                            default -> new VNPayValidateResponse(true, "Unknown status", PaymentStatus.FAILED);
                        };
                    })
                    .orElse(new VNPayValidateResponse(true, "Payment failed", PaymentStatus.FAILED));

        } catch (Exception e) {
            return new VNPayValidateResponse(false, "Payment failed", PaymentStatus.FAILED);
        }
    }

    @Override
    @Transactional
    public VNPayIPNResponse processIPN(Map<String, String> params) {
        try {
            String vnpSecureHash = params.get("vnp_SecureHash");
            Map<String, String> fields = new HashMap<>(params);
            fields.remove("vnp_SecureHashType");
            fields.remove("vnp_SecureHash");
            System.out.println("vnpSecureHash: " + vnpSecureHash);

            // 1. Check Checksum (Code 97)

            if (!vnPayUtil.verifySignature(fields, vnpSecureHash)) {
                return new VNPayIPNResponse("97", "Invalid Checksum");
            }

            String txnRef = params.get("vnp_TxnRef");
            long vnpAmount = Long.parseLong(params.get("vnp_Amount"));
            String responseCode = params.get("vnp_ResponseCode");

            // 2. Check order has exists (code: 01)
            return paymentRepository.findByTransactionId(txnRef)
                    .map(payment -> {
                        Order order = payment.getOrder();

                        // 3. Check money (code: 04) - VNPay x100
                        long dbAmountX100 = order.getTotalMoney()
                                .multiply(new BigDecimal(100))
                                .longValue();

                        if (dbAmountX100 != vnpAmount) {
                            return new VNPayIPNResponse("04", "Invalid Amount");
                        }

                        // 4. Ensure that the order hasn't already been confirmed (code: 02)
                        if (!OrderStatus.PENDING.equals(order.getStatus())) {
                            return new VNPayIPNResponse("02", "Order already confirmed");
                        }

                        // 5. Update Status
                        if ("00".equals(responseCode)) {
                            order.setStatus(OrderStatus.PAID);
                            payment.setStatus(PaymentStatus.SUCCESS);
                        } else {
                            order.setStatus(OrderStatus.FAILED);
                            payment.setStatus(PaymentStatus.FAILED);
                        }

                        orderRepository.save(order);
                        paymentRepository.save(payment);

                        return new VNPayIPNResponse("00", "Confirm Success");
                    })
                    .orElse(new VNPayIPNResponse("01", "Order not Found (Transaction Code invalid)"));

        } catch (Exception e) {
            return new VNPayIPNResponse("99", "Unknown error");
        }
    }
}




