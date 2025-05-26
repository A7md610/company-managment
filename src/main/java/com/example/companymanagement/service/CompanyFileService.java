package com.example.companymanagement.service;

import com.example.companymanagement.entity.Company;
import com.example.companymanagement.entity.CompanyFile;
import com.example.companymanagement.repository.CompanyFileRepository;
import com.example.companymanagement.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class CompanyFileService {
    private final CompanyFileRepository fileRepository;
    private final CompanyRepository companyRepository;
    private final Path fileStorageLocation;

    public CompanyFileService(
            CompanyFileRepository fileRepository,
            CompanyRepository companyRepository,
            @Value("${file.upload-dir}") String uploadDir) {
        this.fileRepository = fileRepository;
        this.companyRepository = companyRepository;
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public CompanyFile storeFile(MultipartFile file, Long companyId) {
        try {
            Company company = companyRepository.findById(companyId)
                    .orElseThrow(() -> new RuntimeException("Company not found"));

            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + fileExtension;

            Path targetLocation = this.fileStorageLocation.resolve(newFilename);
            Files.copy(file.getInputStream(), targetLocation);

            CompanyFile fileEntity = new CompanyFile();
            fileEntity.setName(originalFilename);
            fileEntity.setType(file.getContentType());
            fileEntity.setPath(targetLocation.toString());
            fileEntity.setSize(file.getSize());
            fileEntity.setCompany(company);

            return fileRepository.save(fileEntity);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file. Please try again!", ex);
        }
    }

    public CompanyFile getFile(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found"));
    }

    public void deleteFile(Long fileId) {
        CompanyFile file = getFile(fileId);
        try {
            Files.deleteIfExists(Paths.get(file.getPath()));
            fileRepository.delete(file);
        } catch (IOException ex) {
            throw new RuntimeException("Could not delete file", ex);
        }
    }
} 