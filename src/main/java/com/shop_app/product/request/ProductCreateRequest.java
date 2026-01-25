package com.shop_app.product.request;

import com.shop_app.product.enums.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ProductCreateRequest {
    @Schema(example = "Macbook M2 Pro", required = true)
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String name;

    @Schema(example = "30000000", required = true)
    @PositiveOrZero(message = "The price must be greater than or equal to 0")
    @Max(value = 100000000, message = "The price must be less than or equal to 100,000,000")
    private BigDecimal price;

    @Schema(example = "1", required = true)
    @NotNull(message = "Category' Id is required")
    @PositiveOrZero(message = "Category' Id must be greater than or equal to 0")
    private Long categoryId;

    @Schema(example = "true")
    private ProductStatus status = ProductStatus.ACTIVE;

    private String thumbnail;
    private String description;

    @Schema(description = "png, jpg, jpeg", example = "image.png")
    private List<MultipartFile> files;
}
