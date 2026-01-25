package com.shop_app.product.images.event;

import com.shop_app.product.entity.Product;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class ProductImagesUploadEvent extends ApplicationEvent {

    private final Product product;
    private final List<MultipartFile> files;

    public ProductImagesUploadEvent(Object source,
                                    Product product,
                                    List<MultipartFile> files) {
        super(source);
        this.product = product;
        this.files = files;
    }
}