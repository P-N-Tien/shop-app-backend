package com.shop_app.product.images;

import com.shop_app.product.images.event.ProductImagesUploadEvent;
import com.shop_app.shared.exceptions.InvalidParamException;
import com.shop_app.product.entity.Product;
import com.shop_app.product.images.entity.ProductImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements IProductImageService {
    private static final int MAX_IMAGES_PER_PRODUCT = 5;
    private final ProductImageRepository productImageRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void save(List<MultipartFile> files, Product product) {
        if (files != null && !files.isEmpty() && product != null) {
            throwIfMaxImages(product, files.size());
            List<ProductImage> images = toProductImages(files, product);

            productImageRepository.saveAll(images);

            log.info("[PRODUCT_IMAGES][CREATE]");
        }
    }

    @Override
    public void handleUploadImages(List<MultipartFile> files,
                                   Product product) {

        if (product != null && files != null && !files.isEmpty())
            eventPublisher.publishEvent(
                    new ProductImagesUploadEvent(this, product, files)
            );
    }

    private void throwIfMaxImages(Product product, int newFileCount) {
        int currentCount = productImageRepository.countByProduct(product);
        int totalAfterUpload = currentCount + newFileCount;

        if (totalAfterUpload > MAX_IMAGES_PER_PRODUCT)
            throw new InvalidParamException(
                    String.format("A maximum of %d images per product is allowed.",
                            MAX_IMAGES_PER_PRODUCT)
            );
    }

    private List<ProductImage> toProductImages(
            List<MultipartFile> files, Product product) {
        return files.stream()
                .filter(Objects::nonNull)
                .map((file) -> ProductImage.builder()
                        .imageUrl(file.getOriginalFilename())
                        .product(product)
                        .build())
                .collect(Collectors.toList());
    }
}
