package com.shop_app.user.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class UserResponse {
    private String fullName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String address;
    private boolean isActive;
    private long roleId;
}
