package com.example.companymanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Company {

    @Id
    private Long id; // Custom ID, no auto-generation by the database

    @NotBlank(message = "Company name cannot be empty")
    private String name;

    @NotBlank(message = "City cannot be empty")
    private String city;

    @NotBlank(message = "Employee name cannot be empty")
    private String employee;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompanyFile> files = new ArrayList<>();

    // Default constructor
    public Company() {
        this.id = generateRandomId(); // Generate random ID on creation
    }

    // Generate a 2-digit random ID
    private Long generateRandomId() {
        SecureRandom random = new SecureRandom();
        return (long) (random.nextInt(90) + 10); // Generate a number between 10 and 99
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CompanyFile> getFiles() {
        return files;
    }

    public void setFiles(List<CompanyFile> files) {
        this.files = files;
    }
}