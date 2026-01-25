package com.shop_app.product.response;

import com.shop_app.product.enums.ProductStatus;
import com.shop_app.shared.dto.BaseResponse;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {
    private String name;
    private BigDecimal price;
    private long categoryId;
    private String thumbnail;
    private ProductStatus status;
    private String description;
    private List<MultipartFile> files;
}
