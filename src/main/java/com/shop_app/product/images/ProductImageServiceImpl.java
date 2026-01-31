package com.shop_app.product.images;

import com.shop_app.file_local.FileLocalUploadService;
import com.shop_app.shared.exceptions.InvalidParamException;
import com.shop_app.product.entity.Product;
import com.shop_app.product.images.entity.ProductImage;
import com.shop_app.shared.validate.Validate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {
    private static final int MAX_IMAGES_PER_PRODUCT = 5;
    private final ProductImageRepository repository;
    private final FileLocalUploadService fileUploadService;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void handleSaveFiles(List<MultipartFile> files, Product product)
            throws Exception {
        // 1. Validate inputs
        Validate.notEmpty(files, "Files must not be null or empty");
        Validate.requiredNonNull(product, "Product must not be null");
        validateFileSize(product, files.size());

        // 2. Maintain element order during insertion
        Map<MultipartFile, String> fileToNameMap = new LinkedHashMap<>();

        // 3. Generate unique filenames to avoid conflicts.
        for (MultipartFile file : files) {
            if (fileUploadService.isValidFiles(file)) {
                fileToNameMap.put(file, fileUploadService.generateUniqueFileName(file));
            }
        }

        // 4. Save files to disk(Physical storage) first
        // If an IOException occurs here, the transaction hasn't commited, so no DB impact
        for (Map.Entry<MultipartFile, String> entry : fileToNameMap.entrySet()) {
            fileUploadService.storeFile(entry.getKey(), entry.getValue());
        }

        // 5. Map to entities and persist to database
        List<ProductImage> productImages = fileToNameMap.values().stream()
                .map(fileName -> ProductImage.builder()
                        .product(product)
                        .imageUrl(fileName)
                        .build())
                .toList();

        repository.saveAll(productImages);
    }

    private void validateFileSize(Product product, int newFileCount) {
        int currentCount = repository.countByProduct(product);
        int totalAfterUpload = currentCount + newFileCount;

        if (totalAfterUpload > MAX_IMAGES_PER_PRODUCT)
            throw new InvalidParamException(
                    String.format("A maximum of %d images per product is allowed.",
                            MAX_IMAGES_PER_PRODUCT)
            );
    }
}
