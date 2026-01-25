package com.shop_app.product.images;

import com.shop_app.product.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductImageService {
    void save(List<MultipartFile> files, Product product);

    void handleUploadImages(List<MultipartFile> files, Product product);
}
