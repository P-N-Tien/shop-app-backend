package com.shop_app.product.images;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductImagesDTO {

    @NotBlank(message = "Image url is required")
    @Size(min = 5, message = "Image url must be greater or equal 5 character")
    private String imageUrl;

    @NotNull(message = "Product Id is required")
    @Positive(message = "Product Id must be greater or equal 1")
    private Long productId;
}
