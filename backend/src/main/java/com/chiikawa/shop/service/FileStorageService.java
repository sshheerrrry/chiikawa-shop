package com.chiikawa.shop.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String saveImage(MultipartFile file);
}