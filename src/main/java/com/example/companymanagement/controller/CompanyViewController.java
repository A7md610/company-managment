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

    // Show the "Create New Company" page
    @GetMapping("/new")
    public String createCompanyForm(@RequestParam(value = "success", required = false) String success, Model model) {
        if ("true".equals(success)) {
            model.addAttribute("successMessage", "Company added successfully!");
        }
        model.addAttribute("company", new Company());
        return "create_company"; // Render create_company.html
    }

    // Process the new company creation
    @PostMapping("/new")
    public String saveCompany(@Valid @ModelAttribute("company") Company company,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "create_company"; // Stay on the form page
        }

        companyService.saveCompany(company); // Save the company
        return "redirect:/companies/new?success=true"; // Redirect to avoid duplicate submissions
    }

    // List all companies (Admin page)
    @GetMapping("/admin")
    public String listCompanies(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies()); // Fetch companies from the service
        return "companies"; // Render companies.html
    }

    // Show the edit company form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Company company = companyService.getCompanyById(id);
            model.addAttribute("company", company);
            return "edit_company";
        } catch (RuntimeException e) {
            // Redirect to admin page with an error message (optional)
            return "redirect:/companies/admin?error=notfound";
        }
    }

    // Process the edit company form
    @PostMapping("/edit/{id}")
    public String updateCompany(@PathVariable Long id, @Valid @ModelAttribute("company") Company company, BindingResult result) {
        if (result.hasErrors()) {
            return "edit_company";
        }
        company.setId(id); // Ensure the ID is set
        companyService.saveCompany(company);
        return "redirect:/companies/admin";
    }

    // Delete a company
    @GetMapping("/delete/{id}")
    public String deleteCompany(@PathVariable Long id) {
        try {
            companyService.deleteCompany(id);
        } catch (RuntimeException e) {
            // Optionally, handle the error (e.g., log or redirect with error)
        }
        return "redirect:/companies/admin";
    }

    @GetMapping("/search")
    public String searchByEmployee(@RequestParam("employee") String employee, Model model) {
        model.addAttribute("companies", companyService.findByEmployee(employee));
        return "companies";
    }
}
