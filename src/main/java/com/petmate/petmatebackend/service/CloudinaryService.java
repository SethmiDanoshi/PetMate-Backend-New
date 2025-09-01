package com.petmate.petmatebackend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {
    
    private final Cloudinary cloudinary;
    
    public Map<String, String> uploadPdf(MultipartFile file) {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                    "resource_type", "raw",
                    "folder", "petmate/doctors/licenses",
                    "public_id", "license_" + System.currentTimeMillis()
                )
            );
            
            return Map.of(
                "url", (String) uploadResult.get("secure_url"),
                "publicId", (String) uploadResult.get("public_id")
            );
        } catch (IOException e) {
            log.error("Error uploading PDF to Cloudinary: {}", e.getMessage());
            throw new RuntimeException("Failed to upload PDF", e);
        }
    }
    
    public void deletePdf(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "raw"));
        } catch (IOException e) {
            log.error("Error deleting PDF from Cloudinary: {}", e.getMessage());
        }
    }
}
