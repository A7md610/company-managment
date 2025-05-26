package com.example.companymanagement.controller;

import com.example.companymanagement.entity.CompanyFile;
import com.example.companymanagement.service.CompanyFileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class CompanyFileController {
    private final CompanyFileService fileService;

    public CompanyFileController(CompanyFileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public CompanyFile uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("companyId") Long companyId) {
        return fileService.storeFile(file, companyId);
    }

    @GetMapping("/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        try {
            CompanyFile file = fileService.getFile(fileId);
            Path path = Paths.get(file.getPath());
            Resource resource = new UrlResource(path.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);
        } catch (IOException ex) {
            throw new RuntimeException("Could not download file", ex);
        }
    }

    @GetMapping("/{fileId}/preview")
    public ResponseEntity<Resource> previewFile(@PathVariable Long fileId) {
        try {
            CompanyFile file = fileService.getFile(fileId);
            Path path = Paths.get(file.getPath());
            Resource resource = new UrlResource(path.toUri());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getType()))
                    .body(resource);
        } catch (IOException ex) {
            throw new RuntimeException("Could not preview file", ex);
        }
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) {
        fileService.deleteFile(fileId);
        return ResponseEntity.ok().build();
    }
} 