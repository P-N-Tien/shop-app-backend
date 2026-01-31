package com.shop_app.product.images;

import com.shop_app.product.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductImageService {
    void handleSaveFiles(List<MultipartFile> files, Product product) throws Exception;
}
