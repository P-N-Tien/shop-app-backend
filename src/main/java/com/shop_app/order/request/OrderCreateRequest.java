package com.shop_app.order.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class OrderCreateRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 15, message = "Phone number must between 10 and 15 character")
    private String phoneNumber;

    @PositiveOrZero(message = "Total money must be greater or equal 0")
    private BigDecimal totalMoney;

    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "User' Id not null")
    @Min(value = 1, message = "User' Id must greater or equal 1")
    private Long userId;

    @NotBlank(message = "Shipping Address is required")
    private String shippingAddress;

    private Boolean active = true;
    private LocalDate orderDate = LocalDate.now();
    private String note;
    private String address;
    private String shippingMethod;
    private LocalDate shippingDate;
    private String paymentMethod;
    private String trackingNumber;
}
