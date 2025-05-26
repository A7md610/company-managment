package com.example.companymanagement.repository;

import com.example.companymanagement.entity.CompanyFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyFileRepository extends JpaRepository<CompanyFile, Long> {
} 