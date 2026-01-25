package com.shop_app.auth.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequest(
        @NotBlank(message = "Phone number is required")
        @Size(min = 10, max = 15, message = "Phone number must between 10 and 15 character")
        String phoneNumber,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must have more than 6 character")
        String password
) {
}