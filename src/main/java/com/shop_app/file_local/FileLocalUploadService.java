package com.shop_app.file_local;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileLocalUploadService {
    void uploadToLocalDirectory(List<MultipartFile> file);
}
