package com.shop_app.file_local;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Service
public class FileLocalUploadServiceImpl implements FileLocalUploadService {
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "application/pdf");
    private final String UPLOAD_DIR = "uploads";
    private final long MAX_FILE_SIZE = 50 * 1024 * 1024;

    @Override
    public void uploadToLocalDirectory(List<MultipartFile> files) {
        try {
            createUploadDir();
            saveFilesValid(files);
        } catch (IOException ex) {
            throw new IllegalStateException("Error when creating file: " + ex.getMessage());
        }
    }

    private void createUploadDir() throws IOException {
        Path dirPath = Paths.get(UPLOAD_DIR);
        if (Files.notExists(dirPath)) {
            Files.createDirectory(dirPath);
        }
    }

    private void saveFilesValid(List<MultipartFile> files) throws IOException {
        if (files == null || files.isEmpty()) return;
        for (MultipartFile file : files) {
            if (isValidFiles(file)) {
                storeFile(file);
            }
        }
    }

    private boolean isValidFiles(MultipartFile file) {
        return file != null &&
                ALLOWED_CONTENT_TYPES.contains(file.getContentType()) &&
                file.getSize() <= MAX_FILE_SIZE;
    }

    private void storeFile(MultipartFile file) throws IOException {
        String newFileName = generateTimestampedFileName(file);
        Path filePath = Paths.get(UPLOAD_DIR).resolve(newFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    private String generateTimestampedFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
        return timestamp + "_" + originalFilename;
    }
}
