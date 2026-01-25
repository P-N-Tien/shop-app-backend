package com.shop_app.product.images.event;

import com.shop_app.file_local.FileLocalUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
class ProductImagesUploadListener {
    private final FileLocalUploadService fileUploadService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ProductImagesUploadEvent event) {
        try {
            fileUploadService.uploadToLocalDirectory(event.getFiles());
            log.info("[PRODUCT][UPLOAD_LOCAL_IMAGES] id={}", event.getProduct().getId());
        } catch (Exception ex) {
            log.error("[PRODUCT][ERROR_UPLOADING_IMAGES] id={}", event.getProduct().getId());
        }
    }
}