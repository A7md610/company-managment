package com.example.companymanagement.controller;

import com.example.companymanagement.entity.Company;
import com.example.companymanagement.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/companies")
public class CompanyViewController {

    private final CompanyService companyService;

    public CompanyViewController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // List all companies (Admin page)
    @GetMapping("/admin")
    public String listCompanies(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "companies"; // Render companies.html
    }

    // Show the "Create New Company" page
    @GetMapping("/new")
    public String createCompanyForm(Model model) {
        model.addAttribute("company", new Company());
        return "create_company"; // Render create_company.html
    }

    // Process the new company creation
    @PostMapping("/new")
    public String saveCompany(@Valid @ModelAttribute("company") Company company,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "create_company"; // Reload the form with validation errors
        }

        companyService.saveCompany(company); // Save the company
        model.addAttribute("successMessage", "Company added successfully!");
        model.addAttribute("company", new Company()); // Reset the form
        return "create_company";
    }

    // Show the "Edit Company" page
    @GetMapping("/edit/{id}")
    public String editCompanyForm(@PathVariable Long id, Model model) {
        try {
            Company company = companyService.getCompanyById(id);
            model.addAttribute("company", company);
            return "edit_company"; // Render edit_company.html
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Company not found!");
            return "redirect:/companies/admin"; // Redirect to the list page
        }
    }

    // Process the company update
    @PostMapping("/{id}")
    public String updateCompany(@PathVariable Long id, @Valid @ModelAttribute("company") Company company,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "edit_company"; // Reload the form with validation errors
        }

        company.setId(id);
        companyService.saveCompany(company); // Save the updated company
        return "redirect:/companies/admin"; // Redirect to the list page
    }

    // Delete a company by ID
    @GetMapping("/delete/{id}")
    public String deleteCompany(@PathVariable Long id, Model model) {
        try {
            companyService.deleteCompany(id);
            return "redirect:/companies/admin"; // Redirect to the list page
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Company not found!");
            return "redirect:/companies/admin";
        }
    }
}