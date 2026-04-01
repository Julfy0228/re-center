package com.recenter.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "jpg", "jpeg", "png", "webp", "gif"
    );

    private final Path uploadRoot;

    public FileStorageService() {
        Path currentDir = Paths.get("").toAbsolutePath().normalize();
        Path uploadsPath = currentDir.getFileName() != null
                && "backend".equalsIgnoreCase(currentDir.getFileName().toString())
                ? currentDir.resolve("uploads")
                : currentDir.resolve(Paths.get("backend", "uploads"));

        this.uploadRoot = uploadsPath.normalize();
    }

    public String storeImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        String originalName = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        String extension = extractExtension(originalName);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Unsupported image format");
        }

        try {
            Files.createDirectories(uploadRoot);

            String fileName = UUID.randomUUID() + "." + extension;
            Path target = uploadRoot.resolve(fileName).normalize();

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            }

            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public Path getUploadRoot() {
        return uploadRoot;
    }

    private String extractExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == fileName.length() - 1) {
            throw new IllegalArgumentException("File extension is required");
        }

        return fileName.substring(dotIndex + 1).toLowerCase();
    }
}
