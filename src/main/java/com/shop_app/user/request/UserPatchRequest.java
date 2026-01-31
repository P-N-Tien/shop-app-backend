package com.shop_app.user.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserPatchRequest {

    private String fullName;

    @Size(min = 10, max = 15, message = "Phone number must between 10 and 15 character")
    private String phoneNumber;

    @Size(min = 6, message = "Password must have more than 6 character")
    private String password;

    @Size(min = 6, message = "Confirm password must have more than 6 character")
    private String confirmPassword;

    @Past(message = "Date of birth must be a past day")
    private LocalDate dateOfBirth;

    private String address;

    private Boolean isActive;

    @Min(value = 1, message = "Role' Id must be greater or equal 1")
    private Long roleId;
}


