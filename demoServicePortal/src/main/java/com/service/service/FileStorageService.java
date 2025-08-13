package com.service.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String store(Long professionalId, MultipartFile file, String subfolder) throws Exception;
}