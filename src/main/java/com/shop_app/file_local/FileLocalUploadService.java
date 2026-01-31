package com.shop_app.file_local;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileLocalUploadService {

    String generateUniqueFileName(MultipartFile file) throws IOException;

    boolean isValidFiles(MultipartFile file);

    void storeFile(MultipartFile file, String fileName) throws IOException;
}
