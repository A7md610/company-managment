package com.example.companymanagement.service;

import com.example.companymanagement.entity.Company;
import com.example.companymanagement.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    // Get all companies
    public List<Company> getAllCompanies() {
        return companyRepository.findAll(); // Returns List<Company>
    }

    // Save a new company or update an existing one
    public Company saveCompany(Company company) {
        return companyRepository.save(company); // Saves or updates a company
    }

    // Get a single company by ID
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + id));
    }

    // Delete a company by ID
    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Company not found with ID: " + id);
        }
        companyRepository.deleteById(id); // Deletes the company
    }

    // Check if a company exists by ID
    public boolean existsById(Long id) {
        return companyRepository.existsById(id); // Returns true if company exists
    }
}