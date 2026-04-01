package com.recenter.controller;

import com.recenter.model.dto.UploadResponse;
import com.recenter.service.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/images")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<UploadResponse> uploadImage(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        String fileName = fileStorageService.storeImage(file);
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(fileName)
                .toUriString();

        return ResponseEntity.ok(UploadResponse.builder()
                .fileName(fileName)
                .url(url)
                .build());
    }
}
