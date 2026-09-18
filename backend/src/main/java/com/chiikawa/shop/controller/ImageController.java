package com.chiikawa.shop.controller;

import com.chiikawa.shop.service.FileStorageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/images")
public class ImageController {
    private final FileStorageService fileStorageService;
    private final JwtAuthHelper jwtAuthHelper;

    public ImageController(FileStorageService fileStorageService,JwtAuthHelper jwtAuthHelper) {
        this.fileStorageService = fileStorageService;
        this.jwtAuthHelper = jwtAuthHelper;
    }

    @PostMapping("/upload")
    public Map<String, String> upload(
    		@RequestParam("file")MultipartFile file,
            @RequestHeader(value = "Authorization",required = false)String authorizationHeader) {
        jwtAuthHelper.requireAdmin(authorizationHeader);
        String imageUrl = fileStorageService.saveImage(file);
        return Map.of("imageUrl",imageUrl);
    }
}