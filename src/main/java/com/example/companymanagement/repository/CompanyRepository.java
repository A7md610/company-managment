package com.example.companymanagement.repository;

import com.example.companymanagement.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByEmployeeIgnoreCase(String employee);
}