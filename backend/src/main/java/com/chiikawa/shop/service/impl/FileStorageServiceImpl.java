package com.chiikawa.shop.service.impl;

import com.chiikawa.shop.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final Path uploadPath;
    public FileStorageServiceImpl(@Value("${app.upload.dir}") String uploadDir) {
        this.uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("無法建立圖片資料夾", e);
        }
    }
    
    @Override
    public String saveImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("請選擇圖片");
        }

        String originalName = file.getOriginalFilename();

        if (originalName == null || originalName.isBlank()) {
            throw new IllegalArgumentException("圖片檔名錯誤");
        }

        String lowerName = originalName.toLowerCase();

        boolean validExtension =
                lowerName.endsWith(".jpg") ||
                lowerName.endsWith(".jpeg") ||
                lowerName.endsWith(".png") ||
                lowerName.endsWith(".webp");

        if (!validExtension) {
            throw new IllegalArgumentException("只允許 jpg、jpeg、png、webp 圖片");
        }

        String extension = originalName.substring(originalName.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + extension;
        Path targetPath = uploadPath.resolve(newFileName);

        try {
        	Files.copy(file.getInputStream(),targetPath,StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("圖片儲存失敗",e);
        }
        
        return "/images/" + newFileName;
    }
}