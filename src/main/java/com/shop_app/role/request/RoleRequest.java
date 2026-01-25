package com.shop_app.role.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class RoleRequest {
    @NotBlank(message = "Name is required")
    private String name;
}
