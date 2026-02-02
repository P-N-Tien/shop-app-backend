package com.shop_app.file_local;

import com.shop_app.shared.validate.Validate;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Getter
@Setter
@Slf4j
@Service
public class FileLocalUploadServiceImpl implements FileLocalUploadService {
    private String uploadDir;
    private long maxFileSize;
    private Set<String> allowedContentTypes;

    public boolean isValidFiles(MultipartFile file) {
        return file != null &&
                allowedContentTypes.contains(file.getContentType()) &&
                file.getSize() <= maxFileSize;
    }

    @Override
    public void storeFile(MultipartFile file, String fileName) throws IOException {
        Validate.requiredNonNull(file, "File must not be null");
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public String generateUniqueFileName(MultipartFile file) {
        Validate.requiredNonNull(file, "File must not be null");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
        String originalFilename = file.getOriginalFilename();
        String cleanName = (originalFilename != null) ? originalFilename.replaceAll("\\s+", "_") : "file";
        return String.format("%s-%s", timestamp, cleanName);
    }
}
