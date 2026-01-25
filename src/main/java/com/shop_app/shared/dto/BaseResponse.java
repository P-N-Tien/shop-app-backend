package com.shop_app.shared.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class BaseResponse {
    private long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}