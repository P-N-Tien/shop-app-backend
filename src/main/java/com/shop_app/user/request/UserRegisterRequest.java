package com.shop_app.user.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserRegisterRequest(

        @NotBlank(message = "Phone number is required")
        @Size(min = 10, max = 15, message = "Phone number must between 10 and 15 character")
        String phoneNumber,

        @NotBlank(message = "Full name is required")
        String fullName,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must have more than 6 character")
        String password,

        @NotBlank(message = "Confirm password is required")
        @Size(min = 6, message = "Confirm password must have more than 6 character")
        String confirmPassword,

        @Past(message = "Date of birth must be a past day")
        LocalDate dateOfBirth,

        String address
) {
}


