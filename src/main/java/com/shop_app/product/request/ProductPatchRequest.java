package com.shop_app.product.request;

import com.shop_app.product.enums.ProductStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class ProductPatchRequest {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String name;

    @PositiveOrZero(message = "The price must be greater than or equal to 0")
    @Max(value = 100000000, message = "The price must be less than or equal to 100,000,000")
    private BigDecimal price;

    @PositiveOrZero(message = "Category' Id must be greater than or equal to 0")
    private Long categoryId;

    private String thumbnail;
    private ProductStatus status;
    private String description;
    private List<MultipartFile> files;
}
