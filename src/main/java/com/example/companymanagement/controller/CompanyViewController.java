package com.example.companymanagement.controller;

import com.example.companymanagement.entity.Company;
import com.example.companymanagement.service.CompanyService;
import com.example.companymanagement.service.ExcelService;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/companies")
public class CompanyViewController {

    private final CompanyService companyService;
    private final ExcelService excelService;

    public CompanyViewController(CompanyService companyService, ExcelService excelService) {
        this.companyService = companyService;
        this.excelService = excelService;
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
                              BindingResult result, Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "create_company"; // Stay on the form page
        }

        companyService.saveCompany(company); // Save the company
        redirectAttributes.addFlashAttribute("successMessage", "Company added successfully!");
        return "redirect:/companies/new"; // Redirect without query string
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

    // Upload Excel file
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        try {
            List<Company> companies = excelService.parseExcelFile(file);
            for (Company company : companies) {
                companyService.saveCompany(company);
            }
            return "redirect:/companies/admin?success=upload";
        } catch (IOException e) {
            return "redirect:/companies/admin?error=upload";
        }
    }

    // Download Excel file
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile() throws IOException {
        List<Company> companies = companyService.getAllCompanies();
        ByteArrayInputStream in = excelService.exportToExcel(companies);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=companies.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(in));
    }
}

