package com.example.companymanagement.repository;

import com.example.companymanagement.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}