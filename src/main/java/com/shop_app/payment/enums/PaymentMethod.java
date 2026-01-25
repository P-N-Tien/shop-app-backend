package com.shop_app.payment.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.shop_app.shared.exceptions.InvalidParamException;
import lombok.Getter;

@Getter
public enum PaymentMethod {
    VN_PAY("VN_PAY"),
    COD("COD"),
    MOMO("MOMO");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    @JsonCreator
    public static PaymentMethod fromValue(String value) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.value.equalsIgnoreCase(value)) {
                return method;
            }
        }
        throw new InvalidParamException("Unknown payment method: " + value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
