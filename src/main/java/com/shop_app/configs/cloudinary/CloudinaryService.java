package com.shop_app.configs.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file, String folder) throws IOException {
        Map uploadResult = cloudinary
                .uploader()
                .upload(file.getBytes(),
                        ObjectUtils.asMap(
                                "folder", folder,
                                "resource_type", "auto"
                        ));
        return uploadResult.get("secure_url").toString();
    }

    public void deleteImage(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    public String extractPublicId(String imageUrl) {
        // Extract public_id from Cloudinary URL
        // Example: https://res.cloudinary.com/demo/image/upload/v1234/folder/image.jpg
        // Returns: folder/image
        String[] parts = imageUrl.split("/upload/");
        if (parts.length > 1) {
            String path = parts[1];
            int versionIndex = path.indexOf("/");
            if (versionIndex > 0) {
                path = path.substring(versionIndex + 1);
            }
            return path.substring(0, path.lastIndexOf("."));
        }
        return null;
    }
}