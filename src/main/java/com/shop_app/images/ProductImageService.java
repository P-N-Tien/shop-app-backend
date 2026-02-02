package com.shop_app.images;

import com.shop_app.product.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {
    void handleSaveFiles(List<MultipartFile> files, Product product) throws Exception;


    List<ProductImagesRequest> uploadProductImages(Long productId,
                                                   MultipartFile[] files,
                                                   int isPrimary);
}
