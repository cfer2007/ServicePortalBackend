package com.service.service.impl;

import com.service.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    @Override
    public String store(Long professionalId, MultipartFile file, String subfolder) throws Exception {
        String original = file.getOriginalFilename();
        String ext = original.substring(original.lastIndexOf("."));
        String base = original.substring(0, original.lastIndexOf("."))
                             .replaceAll("[^a-zA-Z0-9_-]", "_");

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
        String unique = UUID.randomUUID().toString().substring(0, 8);
        String filename = timestamp + "_" + unique + "_" + base + ext;

        Path dir = Paths.get("./uploads/professionals/" + professionalId + "/" + subfolder);
        Files.createDirectories(dir);

        Path target = dir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("📂 Archivo guardado en: " + target.toAbsolutePath());

        return "/uploads/professionals/" + professionalId + "/" + subfolder + "/" + filename;
    }


}