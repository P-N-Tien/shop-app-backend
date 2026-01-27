package com.shop_app.role.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserRole {
    ADMIN("ADMIN"),
    USER("USER");

    private String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}